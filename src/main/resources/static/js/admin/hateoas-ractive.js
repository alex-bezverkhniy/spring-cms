String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

multiselectDec = function(node, id){
    var allList, selectedList;
    if(id !== undefined && node.data !== undefined) {

        selectedList = node.data[id];

        // Try to find data
        if(selectedList == undefined) {
            selectedList = node.data[id.substring(0, id.length-1)];
        }

        // No selected data
        if(selectedList == undefined) {
            selectedList = new Array();
        }

        allList = node.data[id+'All'];
        // Try to find All data
        if(allList == undefined) {
            allList = node.data[id+'sAll'];
        }
    }

    if(node.cleanIt || node.data == undefined) {
        selectedList = new Array();
        //allList = new Array();
    }

    if(allList !== undefined && selectedList !== undefined) {
        var dataList = new Array();

        var titleKey = '';

        if(node[0]) {
            titleKey = node[0].attributes['title-key'].nodeValue;
        }

        if(titleKey == undefined) {
            titleKey = 0;
        }

        // If select type is not 'multiple'
        if(typeof node[0] != 'undefined' && typeof node[0].attributes['multiple'] == 'undefined' && allList[0][titleKey] != 'None selected') {
            var option = new Object();
            option[titleKey] = 'None selected';
            option['_links'] = {'self' : {'href' : ''}};
            allList.splice(0,0,option);
        }

        for(i in allList) {
            dataList[i] = {label: allList[i][titleKey], value: allList[i]._links.self.href};
        }

        var ml = $(node).multiselect();
        ml.multiselect('dataprovider', dataList);

        if(selectedList instanceof Array) {
            for(i in selectedList) {
                ml.multiselect('select', selectedList[i]._links.self.href);
            }
        } else {
            if(selectedList._links) {
                ml.multiselect('select', selectedList._links.self.href);
            }
        }

        ml.multiselect('setOptions', {
            enableFiltering: true,
            includeSelectAllOption: true,
            onChange: function(option, checked) {
                multiselectDec.selectedVal = new Array();
                multiselectDec.selectedVal[id] = ml.val();
            }
        });

        ml.multiselect('rebuild');
    }

    return {
        teardown: function () { }
    };
};

HATEOASRactive = Ractive.extend({
    decorators: {
        multiselect: multiselectDec
    },

    selectsData: [{
        allList: new Array(),
        selectedList: new Array()
    }],

    noIntro: true, // disable transitions during initial render
    _bind: function(_data) {
        if(_data) {
            for(var k in _data) {
                this.set(k, eval('_data.'+k));
                //TODO: for CKEditor
                $('#'+k).val(eval('_data.'+k));
            }
        } else {
            for(var k in this.data) {
                if(k !== 'rows') {
                    this.set(k, '');
                    $('#'+k).val('');
                }
            }
        }

    },

    refreshMultiselect: function(key, cleanIt) {
        var node = $('#'+key);

        if(!node.length) {
            node = $('#'+key.substring(0, key.length-1));
        }

        node.data = this.data;
        node.cleanIt = cleanIt;

        // Refresh multiselect
        this.decorators.multiselect(node, key);
    },

    cleanMultiselect: function(key) {
        var node = $('#'+key);
        if(!node.length) {
            node = $('#'+key.substring(0, key.length-1));
        }
        //node.find('option').remove().end();
        node.data = this.data;
        node.cleanIt = true;
        // Refresh multiselect
        this.decorators.multiselect(node, key);
    },

    saveSubResources: function(url, selectedVal, method) {

        var subreportsHrefs = '';

        if(selectedVal instanceof Array) {
            // Concatenate hrefs
            for(i in selectedVal) {
                subreportsHrefs += selectedVal[i] + '\n';
            }
        } else {
            subreportsHrefs = selectedVal;
        }

        jQuery.ajax({
            type: method,
            url: url,
            headers: {
                'Content-Type': 'text/uri-list'
            },
            contentType: "text/uri-list",
            data: subreportsHrefs,

            success: function(data) {
                // Fire update from server

            },

            error: function(error) {
                if(error.status == 201){
                    // Fire update from server
                    self.fire('init');
                } else {
                    alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                }
            }
        });

    },

    save: function(model, method) {
        // Save subresources
        var arraysNamesResources = new Array();

        var self = this;

        for(var k in model) {
            if(k != '_links' && k != 'page') {
                // find subresource
                if(model[k] instanceof  Array || model[k] instanceof  Object) {
                    var url = model._links.self.href + '/' + k;
                    arraysNamesResources.push(k);
                    var selectedVal = '';
                    var d = model[k];
                    if(!(d instanceof Array) && d._links) {
                         k = k + 's';
                    }

                    if(self.decorators.multiselect.selectedVal && self.decorators.multiselect.selectedVal[k]) {
                        self.saveSubResources(url, self.decorators.multiselect.selectedVal[k], method);
                    // if user removed all items
                    } else if(self.decorators.multiselect.selectedVal && typeof self.decorators.multiselect.selectedVal[k] != 'undefined' && self.decorators.multiselect.selectedVal[k] == null && self.data[k] != null){
                        //self.saveSubResources(url, self.data[k][0]._links.self.href, method);
                        self.saveSubResources(url, '', method);
                    }
                }
            }
        }

        // Clean arrays sub resource
        if(arraysNamesResources && arraysNamesResources.length > 0) {
            for(a in arraysNamesResources) {
                delete model[arraysNamesResources[a]];
            }
        }

        var url = self.restURL;
        if(method == 'PUT') {
            url = model._links.self.href;
        }

        jQuery.ajax({
            type: method,
            url: url,
            dataType: 'json',
            headers: {
                'Accept': 'application/json',
                'charset': 'UTF-8',
                'Content-Type': 'application/json'
            },
            contentType: "application/json",
            data: JSON.stringify(model),

            success: function(data) {
                // Fire update from server
                self.fire('init');
            },

            error: function(error) {
                if(error.status == 201){
                    // Fire update from server
                    self.fire('init');
                } else {
                    alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                }
            }
        });
    },

    getByUrl: function(url) {
        var self = this;
        jQuery.ajax({
            type: 'GET',
            url: url,

            success: function(data) {
                self._bind(data);

                // find association
                if(data._links) {
                    for(var k in data._links) {
                        if(k !== 'self') {
                            if(data._links[k].href) {
                                // Get association data
                                self.fire('get_association', data._links[k].href);
                            }
                        }
                    }
                }
            },

            error: function(error) {
                alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
            }
        });
    },

    init: function(){
        var self = this;

        this.on({
            init: function(event){
                jQuery.ajax({
                    type: 'GET',
                    url: self.restURL,
                    success: function(data) {
                        // TODO: will add call of get_all_association
                        var obj0 = data._embedded[self.modelListName][0];
                        if(obj0._links) {
                            for(var k in obj0._links) {
                                if(k != 'self') {
                                    var u = self.restURL.replace(self.modelListName, k);
                                    if(u.indexOf('s', u.length - 's'.length) == -1) {
                                        u = u+'s';
                                    }
                                    self.fire('get_all_association', u);
                                }
                            }
                        }

                        var varModelName = '';
                        if(self.modelListName) {
                            varModelName = self.modelListName
                        } else {
                            varModelName = self.restURL.replace('/', '');
                        }
                        if(data._embedded && data._embedded) {
                            //self.set(varModelName, eval('data._embedded.'+varModelName));
                            self.set('rows', eval('data._embedded.'+varModelName));
                        }
                        if(data.page){
                            self.set('page', data.page);
                        }
                        if(data._links){
                            self.set('_links', data._links);
                        }
                    },
                    error: function(error) {
                        alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                    }
                });
                self.fire('clean');
            },

            submit: function(event){
                var model;

                // stop the page reloading
                if(event) {
                    event.original.preventDefault();
                }
                // Unbind data from form
                model = self.data;//self._unbind();

                // Prepare model for saving
                // Removing arrays
                for(var k in model) {
                    if(model[k] instanceof  Array) {
                        if(k === 'rows' || k.endsWith('All') ) {
                            delete model[k];
                        }
                    }
                }

                var isPut = (model._links.self.href.indexOf('}', model._links.self.href.length - '}'.length) == -1);
                // Update or Create
                if(model._links && model._links.self && model._links.self.href, isPut) {
                    // fire an event - update the model to a server
                    this.fire('put', model);
                } else {
                    // fire an event - create the model to a server
                    this.fire('post', model);
                }

                //self.fire('clean');
            },

            post: function(event){
             var model = event;
             jQuery.ajax({
                 type: 'POST',
                     url: self.restURL,
                     dataType: 'json',
                     headers: {
                         'Accept': 'application/json',
                         'Content-Type': 'application/json'
                     },
                     contentType: "application/json",
                     data: JSON.stringify(model),
                     success: function(data) {
                         // Fire update from server
                         self.fire('init');
                     },

                     error: function(error) {
                         if(error.status == 201){
                             // Fire update from server
                             self.fire('init');
                         } else {
                             alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                         }
                     }
               });
            },

            put: function(event){
                var self = this;
                self.save(event, 'PUT');
            },

            'delete': function(event) {
                var model = self._unbind();

                jQuery.ajax({
                    type: 'DELETE',
                    url: model._links.self.href,

                    success: function(data) {
                        self.fire('init');
                    },

                    error: function(error) {
                        alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                    }
                });
            },

            get: function(event) {
                var url = null;

                if(event.keypath) {
                    var model = self.get(event.keypath);
                    url = model._links.self.href;
                } else {
                    url = event;
                }


                jQuery.ajax({
                    type: 'GET',
                    url: url,

                    success: function(data) {
                        self._bind(data);

                        // find association
                        if(data._links) {
                            for(var k in data._links) {
                                if(k !== 'self') {
                                    if(data._links[k].href) {
                                        // Get association data
                                        self.fire('get_association', data._links[k].href);
                                    }
                                }
                            }
                        }
                    },

                    error: function(error) {
                        alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                    }
                });
            },

            get_association: function(event) {
                var url = event;
                var self = this;

                jQuery.ajax({
                    type: 'GET',
                    url: url,

                    success: function(data) {

                        if(data._embedded){
                            for(var k in data._embedded) {
                                self.set(k, data._embedded[k]);

                                if(data._embedded[k].length > 0) {
                                    // Get association data - full list
                                    //http://localhost:8080/authorities/2
                                    //var u = data._embedded[k][0]._links.self.href;
                                    //u = u.substring(0, u.indexOf(k)+k.length);
                                    var u = self.restURL.replace(self.modelListName, k);
                                    self.fire('get_all_association', u);
                                }
                            }
                        } else {
                            // Clean previous data
                            var key = url.substring(url.lastIndexOf('/')+1, url.length);
                            //self.set(key, new Array());
                            self.set(key, data);
                            var u = self.restURL.replace(self.modelListName, key);
                            //var u = url.substring(0, url.indexOf(self.restURL))+'/'+key;
                            if(data._links){
                                u = u +'s';
                            }
                            self.fire('get_all_association', u);
                        //} else {
                            // Clean data
                          //  self.refreshMultiselect(k);
                        }

                    },

                    error: function(error) {
                        //alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                        console.log('NO associations');
                        //Try to load all associations
                        // Clean previous data
                        var key = url.substring(url.lastIndexOf('/')+1, url.length);
                        var u = self.restURL.replace(self.modelListName, key);
                        u = u +'s';
                        self.fire('get_all_association', u);
                    }
                });
            },

            get_all_association: function(event) {
                var url = event;
                var self = this;

                jQuery.ajax({
                    type: 'GET',
                    url: url,

                    success: function(data) {
                        if(data._embedded){
                            for(var k in data._embedded) {
                                if(k.indexOf('s', k.length - 's'.length) == -1) {
                                    k = k+'s';
                                }
                                self.set(k+'All', data._embedded[k]);
                                self.refreshMultiselect(k, false);

                            }
                        }
                    },

                    error: function(error) {
                        alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                    }
                });
            },

            clean: function(event) {
                var self = this;

                // Clean subresources form elements
                if(self.data) {
                    for(var k in self.data._links) {
                        if(k !== 'self') {
                            if(self.data._links[k].href) {
                                self.refreshMultiselect(k, true);
                            }
                        }
                    }
                }
                // reset the form
                document.activeElement.blur();
                self._bind();
            },

            nextPage: function(event) {
                self.getByUrl(self.data._links.next);
            },

            prevPage: function(event) {
                self.getByUrl(self.data._links.prev);
            }
        });
    }
});
