package com.example.passivepmvc.todoapp.edittask;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passivepmvc.todoapp.R;

/**
 * @author Christian Sarnataro
 *         Created on 05/08/16.
 */
public class EditTaskView extends DrawerLayout {

    private Toolbar toolbar;
    private ActionBar ab;

    private EditText title;
    private EditText description;
    private FloatingActionButton saveTaskButton;

    public EditTaskView(Context context) {
        super(context);
    }

    public EditTaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Initialise widgets using findViewById */
    public void initComponents() {
        setUpToolbar();
        setUpActionBar();
        title = (EditText) findViewById(R.id.add_task_title);
        description = (EditText) findViewById(R.id.add_task_description);
        saveTaskButton = (FloatingActionButton) findViewById(R.id.fab_edit_task);
    }

    public void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New TODO");
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void setUpActionBar() {
        ab = ((AppCompatActivity) getContext()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    public void showMenu() {
        this.openDrawer(GravityCompat.START);

    }

    public void setSaveTaskButtonListener(final OnSaveTaskButtonListener listener) {
        saveTaskButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSaveTaskButtonClicked();
            }
        });
    }

    public void showEmptyTaskError() {
        Snackbar.make(title, getResources().getString(R.string.empty_task_message), Snackbar.LENGTH_SHORT).show();
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void resetFields() {
        title.setText("");
        description.setText("");
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public String getDescription() {
        return description.getText().toString();
    }

    public void showTaskCreatedMessage() {
        Toast.makeText(this.getContext(), getResources().getString(R.string.successfully_saved_task_message), Toast.LENGTH_SHORT)
                .show();
    }

    public void showTaskUpdatedMessage() {
        Toast.makeText(this.getContext(), getResources().getString(R.string.successfully_saved_task_message), Toast.LENGTH_SHORT)
                .show();
    }

    public void showTaskDeletedMessage() {
        Toast.makeText(this.getContext(), getResources().getString(R.string.successfully_deleted_task_message), Toast.LENGTH_SHORT)
                .show();

    }



    public interface OnSaveTaskButtonListener {
        void onSaveTaskButtonClicked();
    }


}
