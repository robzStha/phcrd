package com.gov.phcrevitalization.utils;

import com.gov.phcrevitalization.model.SubItems;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Genre extends ExpandableGroup<SubItems> {

        public Genre(String title, List<SubItems> items) {
            super(title, items);
        }

        @Override
        public List<SubItems> getItems() {
            return super.getItems();
        }
    }