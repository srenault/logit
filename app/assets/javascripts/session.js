(function(window, $) {
    var Session  = function(pseudo, project) {
        this.pseudo = pseudo;
        this.project = project;
    };

    Session.prototype.onReceive = function(log) {
        var projectName = _.template('<%= project %>');
        var messsage = _.template('<%= message %>');
        $('#logs').append('<li>'+ message(log) +'</li>');
    };

    Session.prototype.bindUI = function() {

        var buttons = {
            $start: $('div.commands #start'),
            $stop: $('div.commands #stop')
        }

        var $results = $('#stream');

        buttons.$start.click(function(e) {
            e.preventDefault();
            var url = '/users/{pseudo}/projects/db/{project}/start'.replace('{pseudo}', this.pseudo).replace('{project}', this.project);
            $results.attr('src', url);
        });
        
        buttons.$stop.click(function(e) {
            e.preventDefault();
            $results.attr('src', '#');
        });
    };
    window.Session = Session;
})(window, $);