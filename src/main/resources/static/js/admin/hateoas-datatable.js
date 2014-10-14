/**
 * Data table for HATEOAS REST service
 */
(function( $ ){
    $.fn.hdatatable = function(methodOrOptions) {
        var self = this;
        var tbody = null;
        //var paginationPanel = null;
        var pagination = null;
        var paginationPrev = null;
        var paginationNext = null;
        var paginationBtns = new Array();
        var paginationSize = null;
        var paginationSizeBtn = null;
        var data = null;

        var defaults = {
            nextButtonText: "&raquo;",
            prevButtonText: "&laquo;",
            pageSize: 10
        };

        var methods = {
            init: function(options) {
                window.console.log("INIT");

                tbody = self.children('tbody');

                paginationPanel = $(
                                    '<div class="row" >' +
                                    '   <div id="' + self.attr('id') + 'PaginCol" class="col-lg-6">' +
                                    //'       <ul class="pagination pull-right" style="margin: 0px !important;"></ul>' +
                                    '   </div>' +
                                    '   <div class="col-lg-6">' +
                                    '       <div id="' + self.attr('id') + 'SizeCol" class="input-group pull-left">' +
                                    '           <span class="input-group-addon"><span class="glyphicon glyphicon-th-list" title="Rows per page"></span></span>' +
                                    //'           <input type="text" class="form-control" placeholder="10" style="width: 50px;"/>' +
                                    '       </div>' +
                                    '   </div>'  +
                                    '</div>');
                self.append(paginationPanel);

                var paginCol = $('#'+self.attr('id') + 'PaginCol');
                var sizeCol = $('#'+self.attr('id') + 'SizeCol');


                pagination = $('<ul class="pagination pull-right" style="margin: 0px !important;"></ul>');
                paginCol.append(pagination);

                paginationSize = $('<input type="text" style="width: 50px;" class="form-control"/>').val(settings.pageSize);
                sizeCol.append(paginationSize);

                paginationPrev = $('<li><a href="#">' + settings.prevButtonText + '</a></li>');
                paginationNext = $('<li><a href="#">' + settings.nextButtonText + '</a></li>');

                paginationPrev.click( function() {
                    if(self.data._links.prev) {
                        if(self.data.page.number >= 0) {
                            var url = settings.url+'?page=' + (self.data.page.number - 1) + '&size='+settings.pageSize;
                            methods.ajaxGet(url, methods.updateTBody);
                        }
                    }
                });
                paginationNext.click( function() {
                    if(self.data._links.next) {
                        if(self.data.page.number != self.data.page.totalPages) {
                            var url = settings.url+'?page=' + (self.data.page.number + 1) + '&size='+settings.pageSize;
                            methods.ajaxGet(url, methods.updateTBody);
                        }
                    }
                });
                paginationSize.change( function(e) {
                    if($(this).val()) {
                        settings.pageSize = $(this).val();
                         var url = settings.url+'?page=0&size='+settings.pageSize;

                        methods.ajaxGet(url, methods.show);
                    }
                });

                if(settings != null) {
                    var url = settings.url+'?page=0&size='+settings.pageSize;

                    methods.ajaxGet(url, methods.show);
                    if(self.attr('field-list') != null) {
                        settings.fieldList = self.attr('field-list').split(',');
                    } else if(settings.fieldList) {
                        settings.fieldList = settings.fieldList.split(',');
                    }
                }
            },

            show: function(data) {
                window.console.log("SHOW");

                if(data != null) {
                    // Add pagination panel
                    self.after(paginationPanel);

                    methods.update(data, true);
                }
            },

            hide: function() {
                window.console.log("HIDE");
            },

            updatePagination: function(data, isReload) {
                if(data != null) {
                    self.data = data;
                    if(data.page.number == 0) {
                        paginationPrev.addClass('disabled');
                    } else {
                        paginationPrev.removeClass('disabled');
                    }

                    if(data.page.number+1 == data.page.totalPages) {
                        paginationNext.addClass('disabled');
                    } else {
                        paginationNext.removeClass('disabled');
                    }

                    // pagination buttons
                    if(paginationBtns.length == 0 || isReload) {
                        pagination.empty();
                        pagination.append(paginationPrev);
                        for(i = 0; i <= data.page.totalPages-1; i++) {
                            paginationBtns[i] = $('<li><a href="#">' + (i+1) + '</a></li>');
                            paginationBtns[i].click( function() {
                                var url = settings.url+'?page=' + (this.innerText-1) + '&size='+settings.pageSize;
                                methods.ajaxGet(url, methods.updateTBody);
                            });

                            // Active page
                            if(i == data.page.number) {
                                paginationBtns[i].addClass('active');
                            }
                            pagination.append(paginationBtns[i]);
                        }
                        pagination.append(paginationNext);
                        //pagination.after(paginationSize);
                    } else {
                        for(i = 0; i <= paginationBtns.length-1; i++) {
                            paginationBtns[i].removeClass('active');
                        }
                        paginationBtns[data.page.number].addClass('active');
                    }
                }
            },

            updateTBody:  function(data, isReload) {
                if(data != null) {
                    self.data = data;
                    if(data._embedded) {
                        // Remove all table rows
                        tbody.empty();

                        for(var k in data._embedded) {
                            var tableData = data._embedded[k];
                            if(tableData.length) {
                                for(i = 0; i < tableData.length; i++) {
                                    var row = $('<tr></tr>');
                                    var rowData = tableData[i];
                                    if(settings.fieldList == null) {
                                        for(var kr in rowData) {
                                            if(typeof rowData[kr] !== 'object') {
                                                row.append($('<td>' + rowData[kr] + '</td>'));
                                            }
                                        }
                                    } else {
                                        for(j = 0; j < settings.fieldList.length; j++) {
                                            row.append($('<td>' + rowData[settings.fieldList[j]] + '</td>'));
                                        }
                                    }
                                    tbody.append(row);
                                }
                            }
                        }
                    }
                }
                methods.updatePagination(data, isReload);
            },

            update: function(data) {
                window.console.log("UPDATE");
                self.data = data;
                methods.updateTBody(data);
            },

            ajaxGet: function(url, controller) {
                jQuery.ajax({
                    type: 'GET',
                    url: url,

                    success: controller,

                    error: function(error) {
                        methods.ajaxError(error);
                    }
                });
            },

            ajaxError: function(error) {
                alert('ERROR statusText: ' + error.statusText + ', status: ' + error.status + ', responseText: ' + error.responseText);
            }
        };

        var settings = null;

        // If received argument is object, use it as settings and call "init"
        if(typeof methodOrOptions === 'object') {
            settings = $.extend( {}, defaults, methodOrOptions );
            return methods.init.apply( this, arguments );
        // If received argument is string, call method
        } else if ( methods[methodOrOptions] ) {
            return methods[ methodOrOptions ].apply( this, Array.prototype.slice.call( arguments, 1 ));
        // Default to "init"
        } else if (! methodOrOptions ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  methodOrOptions + ' does not exist on jQuery.tooltip' );
        }
    };

})( jQuery );