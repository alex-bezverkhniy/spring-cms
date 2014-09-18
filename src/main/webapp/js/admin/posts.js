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
    myRactive.restURL = '/posts';
    myRactive.fire( 'init' );

});
