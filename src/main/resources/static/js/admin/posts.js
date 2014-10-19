var converter = new Showdown.converter();

var samplePosts = [
    {
        title: "Hi there!",
        text: "Just sample post",
    }
]

var myRactive = new HATEOASRactive({
    el: '#posts',
    template: '#posts-template',
    data: {rows : samplePosts, renderMarkdown: function (md) { return converter.makeHtml(md); }}
});


$(document).ready(function(){
    $('#posts-template').hide();
    myRactive.restURL = '/rest/posts?page=0&size=10';
    myRactive.modelListName = 'posts';
    myRactive.fire( 'init' );

    edit = function (url){
        myRactive.fire('get', url);

    }
    var hdatatable = $('#datatable').hdatatable({url: '/rest/posts', pageSize: 5, selectCallback: edit, searchUrls: [{title: 'By title', url: '/posts/search/findByTitleLikeIgnoreCase?title='}, {title: 'By text', url: '/posts/search/findByTextLikeIgnoreCase?text='}], columns: [{name: 'title', title: 'Title'},{name: 'text', title: 'Text'}]});
    myRactive.on('init', function() {
        hdatatable.run('refresh');
        window.scrollTo(0, 0);
    });

});
