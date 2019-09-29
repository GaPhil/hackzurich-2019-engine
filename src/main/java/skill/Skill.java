package skill;

import com.google.cloud.language.v1.Token;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Skill {
    private String name;
    private double value;

    public Skill(String name) {
        this.name = name;
    }

    public Skill(Token token, double value) {
        this.name = token.getText().getContent();
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(name, skill.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
