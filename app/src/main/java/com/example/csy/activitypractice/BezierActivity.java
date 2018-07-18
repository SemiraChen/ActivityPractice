package com.example.csy.activitypractice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.github.rubensousa.floatingtoolbar.FloatingToolbarMenuBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BezierActivity extends AppCompatActivity {


    @BindView(R.id.floatingToolbar)
    FloatingToolbar floatingToolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bezier2)
    Bezier2 bezier2;
    @BindView(R.id.rb_control)
    RadioButton rbControl;
    @BindView(R.id.rb_control2)
    RadioButton rbControl2;
    @BindView(R.id.rg_control)
    RadioGroup rgControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        ButterKnife.bind(this);

        rgControl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_control:
                        bezier2.setMode(0);
                        break;
                    case R.id.rb_control2:
                        bezier2.setMode(1);
                        break;
                    default:
                        break;
                }
            }
        });


        floatingToolbar.attachFab(fab);
        floatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {

            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });
        floatingToolbar.addMorphListener(new FloatingToolbar.MorphListener() {
            @Override
            public void onMorphEnd() {

            }

            @Override
            public void onMorphStart() {

            }

            @Override
            public void onUnmorphStart() {

            }

            @Override
            public void onUnmorphEnd() {

            }
        });

        //Create a custom menu
        floatingToolbar.setMenu(new FloatingToolbarMenuBuilder(this)
                .addItem(R.id.action_unread, R.drawable.vector_drawable_add, "Mark unread")
                .addItem(R.id.action_copy, R.drawable.vector_drawable_add, "Copy")
                .addItem(R.id.action_google, R.drawable.vector_drawable_add, "Google+")
                .addItem(R.id.action_facebook, R.drawable.vector_drawable_add, "Facebook")
                .addItem(R.id.action_twitter, R.drawable.vector_drawable_add, "Twitter")
                .build());

    }
}
