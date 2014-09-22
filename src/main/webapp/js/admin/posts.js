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
    myRactive.restURL = '/posts?page=0&size=10';
    myRactive.modelListName = 'posts';
    myRactive.fire( 'init' );


    //$('#datatable').hdatatable({url: '/posts?page=0&size=10'});
    $('#datatable').hdatatable({url: '/posts'});
});
