package com.example.englishapp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //автоматически генерируется
    private int id;

    @OneToMany(mappedBy = "testS", cascade = CascadeType.ALL)
    private List<Statistic> statistics;

    @OneToMany(mappedBy = "testA", cascade = CascadeType.ALL)
    private List<Answer> answers;

    private int numberOfCorrectAnswers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfCorrectAnswers() {
        return numberOfCorrectAnswers;
    }

    public void setNumberOfCorrectAnswers(int numberOfCorrectAnswers) {
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", statistics=" + statistics +
                ", answers=" + answers +
                ", numberOfCorrectAnswers=" + numberOfCorrectAnswers +
                '}';
    }
}
