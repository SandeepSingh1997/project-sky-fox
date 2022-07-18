package com.booking.config.featureTogglz;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum FeatureOptions implements  Feature {

    @EnabledByDefault
    @Label("Show IMDb rating for movie feature")
    SHOW_IMDB_RATING_FOR_MOVIE_FEATURE,

    @EnabledByDefault
    @Label("Change password for admin feature")
    CHANGE_PASSWORD_FOR_ADMIN_FEATURE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
