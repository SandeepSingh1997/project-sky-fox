package com.booking.config.featureTogglz.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class FeatureTogglzController {

    @Autowired
    FeatureManager featureManager;

    @GetMapping("/api/featureToggles")
    public Map<Feature, Boolean> getFeaturesStatus() {
        Set<Feature> featureSet = featureManager.getFeatures();
        Map<Feature, Boolean> featureMap = new HashMap<>();
        for (Feature feature : featureSet) {
            featureMap.put(feature, featureManager.isActive(feature));
        }

        return featureMap;
    }
}
