function Project(name) {
    this.name = name;
}

Project.prototype.refresh = function() {
    var url = "{name}/logs".replace("{name}", "NOWT!FY");
    $.ajax({
        url: url,
        context: $('table.logs'),
        dataType: "json",
        success: function(logs) {
            var this_ = $(this);
            $(this_).empty();
            logs.forEach(function(oneLog) {
                $(this_).append("<tr>");
                for(var dataName in oneLog.data) {
                    if(oneLog.data.hasOwnProperty(dataName) && dataName !='_id' && dataName !='project') {
                        $('fieldset tr').last().append("<td>"+ dataName + "</td>");
                    }
                }
                $(this_).append("</tr>");
                $(this_).append("<tr>");
                for(var dataName in oneLog.data) {
                    if(oneLog.data.hasOwnProperty(dataName) && dataName !='_id' && dataName !='project') {
                        $('fieldset tr').last().append("<td>"+ oneLog.data[dataName] + "</td>");
                    }
                }
                $(this_).append("</tr>");
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("Getting logs failed")
        }
    });
};
