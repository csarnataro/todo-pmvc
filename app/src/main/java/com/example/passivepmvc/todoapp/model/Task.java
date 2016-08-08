/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.passivepmvc.todoapp.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Immutable model class for a Task.
 */
public class Task extends SugarRecord {
    public Long id;
    public String title;
    public String description;
    public boolean completed;

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public Task(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public String getTitleForList() {
        if (title != null && !title.equals("")) {
            return title;
        } else {
            return description;
        }
    }

    public boolean isEmpty() {
        return (title == null || "".equals(title)) &&
                (description == null || "".equals(description));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;


        boolean idEq = (id == null && task.id == null) || (id != null && id.equals(task.id));
        boolean titleEq = (title == null && task.title == null) || (title != null && title.equals(task.title));
        boolean descEq = (description == null && task.description == null) || (description != null && description.equals(task.description));

        return idEq && titleEq || descEq;
    }

    @Override
    public int hashCode() {
        return (id + title + description).hashCode();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }

    public static List<Task> all() {
        return Select.from(Task.class).orderBy("id desc").list();
    }

    public static Task get(Long editedTaskId) {
        // // FIXME: 07/08/16 use a prepareded statement instead
        return Task.findById(Task.class, editedTaskId);
    }
}
