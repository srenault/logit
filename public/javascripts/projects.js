var initSelectionProject = function() {
    $('section.projects ul li').hover(
        function(data) { $(this).text("{ " + $(this).text().replace('{','').replace('}','') + " }")},
        function(data) { $(this).text($(this).text().replace('{ ','').replace(' }',''))}
    );
}

$(document).ready(function() {
    //initSelectionProject();
});

refreshProjects = function() {
    var url = "projects/all";
    $.ajax({
        url: url,
        context: $('section.projects ul'),
        dataType: "json",
        success: function(projects) {
            var this_ = $(this);
            $(this_).empty();
            projects.forEach(function(oneProject) {
                $(this_).append('<li><a href="projects/'+oneProject.name+'">'+oneProject.name+'</a></li>');
            });
            //initSelectionProject();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("Getting projects failed");
        }
    });
};
