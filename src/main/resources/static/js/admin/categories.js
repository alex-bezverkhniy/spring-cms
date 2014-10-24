var converter = new Showdown.converter();

var sampleCategories = [
    {
        title: "Hi there!",
        text: "Just sample post",
    }
]

var myRactive = new HATEOASRactive({
    el: '#categories',
    template: '#categories-template',
    data: {rows : sampleCategories, renderMarkdown: function (md) { return converter.makeHtml(md); }}
});


$(document).ready(function(){
    $('#categories-template').hide();
    myRactive.restURL = '/rest/categories';
    myRactive.modelListName = 'categories';
    myRactive.fire( 'init' );

    edit = function (url){
        myRactive.fire('get', url);

    }
    var hdatatable = $('#datatable').hdatatable({url: '/rest/categories', pageSize: 5, selectCallback: edit, searchUrls: [{title: 'By title', url: '/categories/search/findByTitleLikeIgnoreCase?title='}, {title: 'By text', url: '/categories/search/findByTextLikeIgnoreCase?text='}], columns: [{name: 'title', title: 'Title'},{name: 'text', title: 'Text'}]});
    myRactive.on('init', function() {
        hdatatable.run('refresh');
        window.scrollTo(0, 0);
    });

});
