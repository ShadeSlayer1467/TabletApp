package shadeslayer.omniapp.hobbytracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import shadeslayer.omniapp.R;
import shadeslayer.omniapp.hobbytracker.Models.PracticeSession;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionViewHolder> {

    private List<PracticeSession> sessions;

    public SessionsAdapter(List<PracticeSession> sessions) {
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        PracticeSession session = sessions.get(position);
        holder.startTimeTextView.setText(session.getStartTime());
        holder.endTimeTextView.setText(session.getEndTime());
        holder.durationTextView.setText(String.valueOf(session.getDuration()));
        holder.notesTextView.setText(session.getNotes());
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeTextView;
        TextView endTimeTextView;
        TextView durationTextView;
        TextView notesTextView;

        SessionViewHolder(View itemView) {
            super(itemView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
        }
    }
}
