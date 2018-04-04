package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/4/004.
 */

public class TaskPoint implements Serializable{


    /**
     * questions : [{"answer":"string","choice":false,"description":"string","fileUrl":"string","id":0,"questionType":0,"rightAnswer":"string","scenicId":0,"score":0,"state":false,"typeCategory":"string"}]
     * task : {"description":"string","fileUrl":"string","gamePointId":0,"id":0,"score":0,"taskType":0}
     */

    private TaskBean task;
    private List<QuestionsBean> questions;

    public TaskBean getTask() {
        return task;
    }

    public void setTask(TaskBean task) {
        this.task = task;
    }

    public List<QuestionsBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsBean> questions) {
        this.questions = questions;
    }

    public static class TaskBean implements Serializable {
        /**
         * description : string
         * fileUrl : string
         * gamePointId : 0
         * id : 0
         * score : 0
         * taskType : 0
         */

        private String description;
        private String fileUrl;
        private int gamePointId;
        private int id;
        private int score;
        private int taskType;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getGamePointId() {
            return gamePointId;
        }

        public void setGamePointId(int gamePointId) {
            this.gamePointId = gamePointId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getTaskType() {
            return taskType;
        }

        public void setTaskType(int taskType) {
            this.taskType = taskType;
        }
    }

    public static class QuestionsBean {
        /**
         * answer : string
         * choice : false
         * description : string
         * fileUrl : string
         * id : 0
         * questionType : 0
         * rightAnswer : string
         * scenicId : 0
         * score : 0
         * state : false
         * typeCategory : string
         */

        private String answer;
        private boolean choice;
        private String description;
        private String fileUrl;
        private int id;
        private int questionType;
        private String rightAnswer;
        private int scenicId;
        private int score;
        private boolean state;
        private String typeCategory;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public boolean isChoice() {
            return choice;
        }

        public void setChoice(boolean choice) {
            this.choice = choice;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuestionType() {
            return questionType;
        }

        public void setQuestionType(int questionType) {
            this.questionType = questionType;
        }

        public String getRightAnswer() {
            return rightAnswer;
        }

        public void setRightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
        }

        public int getScenicId() {
            return scenicId;
        }

        public void setScenicId(int scenicId) {
            this.scenicId = scenicId;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public String getTypeCategory() {
            return typeCategory;
        }

        public void setTypeCategory(String typeCategory) {
            this.typeCategory = typeCategory;
        }
    }
}
