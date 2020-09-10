package com.app.manager.model.returnResult;


import com.app.manager.entity.History;
import org.springframework.http.HttpStatus;

public class MigrationQueryResult {
    private History.EMigration result;
    private String description;
    private HttpStatus http_status;
    private Object content;

    public MigrationQueryResult() {
    }

    public MigrationQueryResult(History.EMigration result,
                                String description, HttpStatus httpstatus,
                                Object content) {
        this.result = result;
        this.description = description;
        http_status = httpstatus;
        this.content = content;
    }

    public HttpStatus getHttp_status() {
        return http_status;
    }

    public void setHttp_status(HttpStatus http_status) {
        this.http_status = http_status;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public History.EMigration getResult() {
        return result;
    }

    public void setResult(History.EMigration result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
