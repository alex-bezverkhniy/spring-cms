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
    var hdatatable = $('#datatable').hdatatable({
        url: '/rest/users',
        pageSize: 5,
        selectCallback: edit,
        searchUrls: [
            {title: 'By Username', url: '/rest/users/search/findByUsernameLikeIgnoreCase?name='}
        ],
        columns: [
            {name: 'username', title: 'User Name'},
            {name: 'accountNonExpired', title: 'Account Non Expired'},
            {name: 'accountNonLocked', title: 'Account Non Locked'},
            {name: 'credentialsNonExpired', title: 'Credentials Non Expired'},
            {name: 'enabled', title: 'Enabled'}
        ]
    });
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
    myRactive.restURL = '/rest/users';
    myRactive.modelListName = 'users';
    myRactive.fire( 'init' );

});
