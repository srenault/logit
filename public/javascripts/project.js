refreshProject = function(name) {
    var url = "{name}/logs".replace("{name}", name);
    $.ajax({
        url: url,
        context: $('section.logs table'),
        dataType: "json",
        success: function(logs) {
            var this_ = $(this);
            $(this_).empty();
            logs.forEach(function(oneLog) {
                $(this_).append("<tr>");
                for(var dataName in oneLog.data) {
                    if(oneLog.data.hasOwnProperty(dataName) && dataName !='_id' && dataName !='project') {
                        $('fieldset tr').last().append("<th>"+ dataName + "</th>");
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
