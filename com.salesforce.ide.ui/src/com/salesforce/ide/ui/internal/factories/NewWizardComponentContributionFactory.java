/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.ui.internal.factories;

import java.io.IOException;
import java.io.InputStream;

import com.salesforce.ide.core.internal.utils.Constants;
import com.salesforce.ide.core.internal.utils.FileReader;
import com.salesforce.ide.core.internal.utils.Utils;
import com.salesforce.ide.core.model.Component;
import com.salesforce.ide.ui.ForceIdeUIPlugin;

public class NewWizardComponentContributionFactory extends ComponentContributionFactory {

    public NewWizardComponentContributionFactory() {
        super();
    }

    @Override
    protected String getContributionTemplate() throws IOException {
        if (Utils.isEmpty(contributionTemplate)) {
            throw new IllegalArgumentException("Contribution template cannot be null");
        }

        InputStream actionStream = ForceIdeUIPlugin.getResourceStreamEntry(contributionTemplate);
        return FileReader.getTemplateContent(actionStream);
    }

    @Override
    protected boolean isValidComponentForContribution(Component component) {
        return true;
    }

    @Override
    protected String getExtensionPoint() throws IOException {
        return "org.eclipse.ui.newWizards";
    }

    @Override
    protected String replaceTokens(String template, Component component, String wizardClassName) {
        String tmpTemplate = new String(template);
        return tmpTemplate.replaceAll("@@OBJECT_NAME@@", component.getComponentType()).replaceAll("@@BRAND@@",
            Constants.BRAND_NAME).replaceAll("@@DEFAULT_FOLDER@@", component.getDefaultFolder()).replaceAll(
            "@@DISPLAY_NAME@@", component.getDisplayName()).replaceAll("@@FULL_CLASS_NAME@@", wizardClassName);
    }
}
