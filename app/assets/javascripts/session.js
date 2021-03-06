(function(window, $) {
    var Session  = function(pseudo, project) {
        this.pseudo = pseudo;
        this.project = project;
    };

    Session.prototype.onReceive = function(log) {
        alert("log received !");
        var projectName = _.template('<%= project %>');
        var messsage = _.template('<%= data.message %>');
        $('#logs').append('<li>'+ log.data.message +'</li>');
    };

    Session.prototype.bindUI = function() {

        var buttons = {
            $start: $('div.commands #start'),
            $stop: $('div.commands #stop')
        };

        var $results = $('#stream');
        var _this = this;
        buttons.$start.click(function(e) {
            e.preventDefault();
            var url = '/users/{pseudo}/projects/db/{project}/start'.replace('{pseudo}', _this.pseudo).replace('{project}', _this.project);
            $results.attr('src', url);
        });
        
        buttons.$stop.click(function(e) {
            e.preventDefault();
            $results.attr('src', '#');
        });
    };
    window.Session = Session;
})(window, $);