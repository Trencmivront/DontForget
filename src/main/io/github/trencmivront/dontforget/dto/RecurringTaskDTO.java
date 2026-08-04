package main.io.github.trencmivront.dontforget.dto;

import main.io.github.trencmivront.dontforget.entities.RecurringTask;

public class RecurringTaskDTO {

    private Long taskId;
    private Long weekDayId;

    public RecurringTaskDTO() {}

    public RecurringTaskDTO(Long taskId, Long weekDayId) {
        this.taskId = taskId;
        this.weekDayId = weekDayId;
    }

    public RecurringTaskDTO(RecurringTask recurringTask) {
        this.taskId = recurringTask.getTaskId();
        this.weekDayId = recurringTask.getWeekDayId();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getWeekDayId() {
        return weekDayId;
    }

    public void setWeekDayId(Long weekDayId) {
        this.weekDayId = weekDayId;
    }
}
