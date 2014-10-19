var sampleusers = [
    {
        title: "Hi there!",
        text: "Just sample post",
    }
]

var myRactive = new HATEOASRactive({
    el: '#users',
    template: '#users-template',
    data: {rows : sampleusers, renderMarkdown: function (md) { return converter.makeHtml(md); }}
});


$(document).ready(function(){
    $('#users-template').hide();
    myRactive.restURL = '/rest/users?page=0&size=10';
    myRactive.modelListName = 'users';
    myRactive.fire( 'init' );

    edit = function (url){
        myRactive.fire('get', url);

    }
    var hdatatable = $('#datatable').hdatatable({url: '/rest/users', pageSize: 5, selectCallback: edit, searchUrls: [{title: 'By title', url: '/rest/users/search/findByTitleLikeIgnoreCase?title='}, {title: 'By text', url: '/rest/users/search/findByTextLikeIgnoreCase?text='}], columns: [{name: 'title', title: 'Title'},{name: 'text', title: 'Text'}]});
    myRactive.on('init', function() {
        hdatatable.run('refresh');
        window.scrollTo(0, 0);
    });

});


var converter = new Showdown.converter();

var sampleUsers = [
    {
        username: "admin",
        password: "admin",
        accountNonExpired: true,
        accountNonLocked: true,
        credentialsNonExpired: true
    }
]

var myRactive = new HATEOASRactive({
    el: '#users',
    template: '#users-template',
    data: {rows : sampleUsers, renderMarkdown: function (md) { return converter.makeHtml(md); }}
});


$(document).ready(function(){
    $('#users-template').hide();
    myRactive.restURL = '/users';
    myRactive.modelListName = 'authors';
    myRactive.fire( 'init' );

});
