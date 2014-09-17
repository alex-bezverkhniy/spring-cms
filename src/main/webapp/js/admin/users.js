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
