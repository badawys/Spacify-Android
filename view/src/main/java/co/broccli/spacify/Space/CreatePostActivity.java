package co.broccli.spacify.Space;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import co.broccli.logic.Callback;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.space.CreatePost;
import co.broccli.spacify.R;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        final FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.send_post);
        final int SpaceId = getIntent().getIntExtra("EXTRA_SPACE_ID", 0);

        final TextView postText = (TextView) findViewById(R.id.postText);

        final ProgressDialog progressDialog = new ProgressDialog(CreatePostActivity.this,
                R.style.AppTheme_Dark_Dialog);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.create_space_wait_message));
                progressDialog.show();
                final String text = postText.getText().toString();
                SpacifyApi.space().createNewPost(CreatePostActivity.this,
                        1,
                        SpaceId,
                        text,
                        new Callback<CreatePost>() {
                            @Override
                            public void onResult(CreatePost createPost) {
                                progressDialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(CreatePostActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }
                        });
            }
        });

    }
}
