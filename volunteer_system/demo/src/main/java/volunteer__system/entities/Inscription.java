package volunteer__system.entities;

import java.util.Date;

public class Inscription {
    private int id;
    private int user_id;
    private int project_id;
    private Date current_date;

    public Inscription(int user_id, int project_id, Date current_date) {
        this.user_id = user_id;
        this.project_id = project_id;
        this.current_date = current_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public Date getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(Date current_date) {
        this.current_date = current_date;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inscription{");
        sb.append("id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", project_id=").append(project_id);
        sb.append(", current_date=").append(current_date);
        sb.append('}');
        return sb.toString();
    }

}
