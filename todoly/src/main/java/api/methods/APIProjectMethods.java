package api.methods;

import entities.projects.NewProject;
import entities.projects.Project;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APIProjectMethods extends Method {

    public static  Project createProject(String content, int icon) {
        NewProject project = new NewProject(content, icon);
        String projectsEndpoint = environment.getProjectsEndpoint();
        Response response = apiManager.post(projectsEndpoint, ContentType.JSON, project);
        Project responseProject = response.as(Project.class);

        if (responseProject.getId() != null) {
            return responseProject;
        } else {
            log.error("Unable to create a new project");
            return null;
        }
    }

    public static boolean deleteProject(int projectId) {
        log.info("Deleting Project " + projectId);
        String projectByIdEndpoint = String.format(environment.getProjectByIdEndpoint(), projectId);
        Response response = apiManager.delete(projectByIdEndpoint);
        Project responseProject = response.as(Project.class);

        if (responseProject.getDeleted() != null) {
            return responseProject.getDeleted();
        } else {
            log.error("Unable to complete request to delete Project " + projectId);
            return false;
        }
    }
}
