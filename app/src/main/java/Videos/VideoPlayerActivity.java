package Videos;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.niko.pruebaurgencias3.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // Inicializar el PlayerView
        playerView = findViewById(R.id.player_view);

        // Obtener el ID del recurso de video pasado desde el intent
        int videoResourceId = getIntent().getIntExtra("videoResourceId", -1);

        if (videoResourceId != -1) {
            // Crear un ExoPlayer y configurarlo
            exoPlayer = new ExoPlayer.Builder(this).build();

            // Crear un MediaItem con la ruta del video
            String videoPath = "android.resource://" + getPackageName() + "/" + videoResourceId;
            MediaItem mediaItem = MediaItem.fromUri(videoPath);

            // Configurar el ExoPlayer con el MediaItem
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();

            // Asociar el player al PlayerView
            playerView.setPlayer(exoPlayer);
        } else {
            Toast.makeText(this, "No video resource available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausar el ExoPlayer si la actividad se pausa
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar recursos del ExoPlayer
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}
