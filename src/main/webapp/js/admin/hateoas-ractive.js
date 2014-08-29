String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

multiselectDec = function(node, id){
    var allList, selectedList;
    if(id !== undefined && node.data !== undefined) {
        selectedList = node.data[id];
        allList = node.data[id+'All'];
    }

    if(allList !== undefined && selectedList !== undefined) {
        var dataList = new Array();

        var titleKey = node[0].attributes['title-key'].nodeValue;
        if(titleKey == undefined) {
            titleKey = 0;
        }

        for(i in allList) {
            dataList[i] = {label: allList[i][titleKey], value: allList[i]._links.self.href};
        }


        var ml = $(node).multiselect();
        ml.multiselect('dataprovider', dataList);

        for(i in selectedList) {
            ml.multiselect('select', selectedList[i]._links.self.href);
        };


        ml.multiselect('setOptions', {
            enableFiltering: true,
            includeSelectAllOption: true,
            onChange: function(event) {
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
            }
        } else {
            for(var k in this.data) {
                if(k !== 'rows') {
                    this.set(k, '');
                }
            }
        }

    },

    refreshMultiselect: function(key) {
        var node = $('#'+key);
        node.data = this.data;

        // Refresh multiselect
        this.decorators.multiselect(node, key);
    },

    save: function(model, method) {
        // Save subresources
        var arraysNamesResources = new Array();

        var self = this;

        for(var k in model) {
            if(model[k] instanceof  Array) {
                var url = model._links.self.href + '/' + k;
                arraysNamesResources.push(k);
                var selectedVal = '';
                if(self.decorators.multiselect.selectedVal) {
                    selectedVal = self.decorators.multiselect.selectedVal[k];
                }
                if(selectedVal) {
                    var subreportsHrefs = '';
                    // Concatenate hrefs
                    for(i in selectedVal) {
                        subreportsHrefs += selectedVal[i] + '\n';
                    }

                    if(subreportsHrefs) {
                        jQuery.ajax({
                            type: 'PUT',
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

        jQuery.ajax({
            type: method,
            url: model._links.self.href,
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

    init: function(){
        var self = this;

        this.on({
            init: function(event){
                jQuery.ajax({
                    type: 'GET',
                    url: self.restURL,
                    success: function(data) {
                        var varModelName = self.restURL.replace('/', '');
                        if(data._embedded && data._embedded) {
                            //self.set(varModelName, eval('data._embedded.'+varModelName));
                            self.set('rows', eval('data._embedded.'+varModelName));
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
                event.original.preventDefault();
                // Unbind data from form
                model = self.data;//self._unbind();

                // Prepare model for saving
                // Removing arrays
                for(var k in model) {
                    if(model[k] instanceof  Array) {
                        if(k === 'rows' || k.endsWith('All')) {
                            delete model[k];
                        }
                    }
                }

                // Save subresouces


                // Update or Create
                if(model._links && model._links.self && model._links.self.href) {
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
                var model = self.get(event.keypath);

                jQuery.ajax({
                    type: 'GET',
                    url: model._links.self.href,

                    success: function(data) {
                        self._bind(data);

                        // find association
                        if(data._links) {
                            for(var k in data._links) {
                                if(k !== 'self') {
                                    // Get association data
                                    self.fire('get_association', data._links[k].href);
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
                                    var u = data._embedded[k][0]._links.self.href;
                                    u = u.substring(0, u.indexOf(k)+k.length);
                                    self.fire('get_all_association', u);
                                }
                            }
                        } else {
                            // Clean previous data
                            var key = url.substring(url.lastIndexOf('/')+1, url.length);
                            self.set(key, new Array());

                            self.refreshMultiselect(key);
                            /*
                            var node = $('#'+key);
                            node.data = self.data;

                            // Refresh multiselect
                            self.decorators.multiselect(node, key);
                            */

                        }
                    },

                    error: function(error) {
                        alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
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
                                self.set(k+'All', data._embedded[k]);

                                self.refreshMultiselect(k);

                            }
                        }
                    },

                    error: function(error) {
                        alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
                    }
                });
            },

            clean: function(event) {
                // reset the form
                document.activeElement.blur();
                self._bind();
            }
        });
    }
});
