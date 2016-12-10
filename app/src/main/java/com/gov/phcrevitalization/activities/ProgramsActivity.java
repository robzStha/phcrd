package com.gov.phcrevitalization.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gov.phcrevitalization.R;
import com.gov.phcrevitalization.model.GenreMenuFactory;
import com.gov.phcrevitalization.model.SubItems;
import com.gov.phcrevitalization.utils.Genre;
import com.gov.phcrevitalization.utils.Opener;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ProgramsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        recyclerView = (RecyclerView) findViewById(R.id.rv_programs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ExpandableAdapters(GenreMenuFactory.makeGenre()));

    }

    private class ExpandableAdapters extends ExpandableRecyclerViewAdapter<MenuItemHolder, SubMenuItemHolder> {

        public ExpandableAdapters(List<? extends ExpandableGroup> groups) {
            super(groups);
        }

        @Override
        public MenuItemHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_menu_item, parent, false);

            return new MenuItemHolder(view);
        }

        @Override
        public SubMenuItemHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_sub_menu_item, parent, false);

            return new SubMenuItemHolder(view);
        }

        @Override
        public void onBindChildViewHolder(SubMenuItemHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
            final Genre subMenuItemObj = (Genre) group;
            final String subMenuTitle= subMenuItemObj.getItems().get(childIndex).getSubMenuItem();
            holder.tvSubItem.setText(subMenuItemObj.getItems().get(childIndex).getSubMenuItem());
            holder.tvSubItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ProgramsActivity.this, "TItle: "+subMenuTitle, Toast.LENGTH_SHORT).show();
                    if (subMenuTitle.equals(GenreMenuFactory.SUBMENU11)) {
                        Opener.LocalWebView(ProgramsActivity.this, "free_essential_drugs_procurement");
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU12)) {
                        Opener.LocalWebView(ProgramsActivity.this, "meeting_of_district_level_monitoring_committee");
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU21)) {
                        Opener.LocalWebView(ProgramsActivity.this, "regional_level_orientation_training");
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU22)) {
                        Opener.LocalWebView(ProgramsActivity.this, "district_level_orientation_training");
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU71)) {
                        Opener.LocalWebView(ProgramsActivity.this, "district_level_social_audit_program");
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU72)) {
                        Opener.LocalWebView(ProgramsActivity.this, "orientation_on_social_audit_program");
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU91)) {
                        Opener.LocalWebView(ProgramsActivity.this, "donation_for_building_construction_for_urban_health_centres");
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU101)) {
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU102)) {
                    } else if (subMenuTitle.equals(GenreMenuFactory.SUBMENU103)) {
                    }
                }
            });
        }

        @Override
        public void onBindGroupViewHolder(MenuItemHolder holder, final int flatPosition, final ExpandableGroup group) {
            holder.setTvItem(group.getTitle());
            if (group.getItemCount() == 0) {
                holder.tvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (group.getTitle().equals(GenreMenuFactory.MENU3)) {
                            Opener.LocalWebView(ProgramsActivity.this, "donation_for_printing_reg_fee_for");
                        } else if (group.getTitle().equals(GenreMenuFactory.MENU4)) {
                            Opener.LocalWebView(ProgramsActivity.this, "reporting_program_for_target_group");
                        } else if (group.getTitle().equals(GenreMenuFactory.MENU5)) {
                            Opener.LocalWebView(ProgramsActivity.this, "special_health_program_for_dag_vdcs");
                        } else if (group.getTitle().equals(GenreMenuFactory.MENU8)) {
                            Opener.LocalWebView(ProgramsActivity.this, "community_health_unit");
                        } else if (group.getTitle().equals(GenreMenuFactory.MENU11)) {

                            Toast.makeText(ProgramsActivity.this, "Position: " + group.getTitle(), Toast.LENGTH_SHORT).show();
                        } else if (group.getTitle().equals(GenreMenuFactory.MENU12)) {
                            Toast.makeText(ProgramsActivity.this, "Position: " + group.getTitle(), Toast.LENGTH_SHORT).show();
                        } else if (group.getTitle().equals(GenreMenuFactory.MENU13)) {
                            Toast.makeText(ProgramsActivity.this, "Position: " + group.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private class MenuItemHolder extends GroupViewHolder {
        TextView tvItem;
        ImageView ivChevron;

        public MenuItemHolder(View itemView) {
            super(itemView);
            this.tvItem = (TextView) itemView.findViewById(R.id.tv_menu_item);
            this.ivChevron = (ImageView) itemView.findViewById(R.id.iv_chevron);
        }

        public void setTvItem(String item) {
            this.tvItem.setText(item);
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            ivChevron.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            ivChevron.setAnimation(rotate);
        }
    }


    private class SubMenuItemHolder extends ChildViewHolder {
        TextView tvSubItem;

        public SubMenuItemHolder(View itemView) {
            super(itemView);
            this.tvSubItem = (TextView) itemView.findViewById(R.id.tv_sub_menu_item);
        }

        public void setTvSubItem(TextView tvSubItem) {
            this.tvSubItem = tvSubItem;
        }
    }


}
