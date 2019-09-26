package cn.usr.cloud.alarm.entity;

import lombok.Data;

import java.util.Observable;

/**
 * Created by pi on 2018/9/19.
 */
@Data
public class BaseEntity extends Observable {
    private Integer id;

    public void notifyObservers() {
        // 注意：拦截器中使用set作为关键字，super.setChanged() 不会被拦截，this.setChanged()会被拦截 引起拦截器递归调用
        super.setChanged();
        super.notifyObservers(this.getClass());

    }
}
