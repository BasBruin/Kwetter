package nl.kwetter2.tweetservice.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequest {
    private String title;
    private String body;

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }
}


