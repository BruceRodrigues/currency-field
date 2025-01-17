/*
 * Copyright 2015 Kerim O.D.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tm.kod.widgets.demo;

import com.google.gwt.thirdparty.guava.common.io.BaseEncoding;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import tm.kod.widgets.numberfield.NumberField;
import tm.kod.widgets.webcamjs.WebCamJS;

/**
 *
 * @author Kerim O.D.
 */
public class WebCamJSView extends VerticalLayout implements View {

    WebCamJS webCam = new WebCamJS("Webcam");
    Image image = new Image("Image");
    HorizontalLayout buttons = new HorizontalLayout();

    public WebCamJSView() {
        setSizeFull();
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setMargin(true);
        content.setSizeUndefined();
        addComponent(content);
        setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        Label label = new Label("WebCamJSJS on Vaadin Demo");
        label.addStyleName("h1");
        content.addComponent(label);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        content.addComponent(layout);
        image.setHeight(240, Sizeable.Unit.PIXELS);
        image.setWidth(320, Sizeable.Unit.PIXELS);
        webCam.addReadyListener(new WebCamJS.ReadyListener() {

            @Override
            public void ready(WebCamJS.ReadyEvent e) {
                buttons.setVisible(true);
            }
        });
        webCam.addSnapReceivedListener(new WebCamJS.SnapReceivedListener() {

            @Override
            public void received(final WebCamJS.ReceivedEvent e) {
                StreamResource sr = new StreamResource(new StreamResource.StreamSource() {

                    @Override
                    public InputStream getStream() {
                        byte[] bytes = BaseEncoding.base64().decode(e.getBase64String());
                        return new ByteArrayInputStream(bytes);
                    }
                }, System.currentTimeMillis() + ".jpeg");
                image.setSource(sr);
            }
        });
        layout.addComponent(webCam);
        layout.addComponent(image);
        layout.addComponent(createConfiguration());
        // initing buttons
        buttons.setVisible(false); // visible when webcam is ready
        buttons.setSpacing(true);
        content.addComponent(buttons);
        // init freeze button
        Button button = new Button("Freeze", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                webCam.freeze();
            }
        });
        buttons.addComponent(button);
        // initing unfreeze button
        button = new Button("Unfreeze", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                webCam.unfreeze();
            }
        });
        buttons.addComponent(button);
        // initing snapshoot button
        button = new Button("Upload (Snap)", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                webCam.snap();
            }
        });
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        buttons.addComponent(button);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Page.getCurrent().setTitle("WebCamJS Demo View");
    }

    private VerticalLayout createConfiguration() {
        VerticalLayout layout = new VerticalLayout();
        Label label = new Label("Configuration");
        label.addStyleName("h2 colored");
        layout.addComponent(label);
        FormLayout form = new FormLayout(); 
        form.setWidth(350, Unit.PIXELS);
        layout.addComponent(form);
        // cam width
        final NumberField camWidth = new NumberField("Cam width(pixels)");
        camWidth.setConverter(Integer.class);
        camWidth.setConvertedValue(webCam.getCamWidth());
        camWidth.setWidth(100, Unit.PERCENTAGE);
        form.addComponent(camWidth);
        // cam height
        final NumberField camHeight = new NumberField("Cam height(pixels)");
        camHeight.setConverter(Integer.class);
        camHeight.setConvertedValue(webCam.getCamHeight());
        camHeight.setWidth(100, Unit.PERCENTAGE);
        form.addComponent(camHeight);
        // image quality
        final Slider quality = new Slider("Image Quality(0-100)", 0, 100);
        quality.setWidth(100, Unit.PERCENTAGE);
        quality.setValue(new Double(webCam.getQuality()));
        form.addComponent(quality);
        Button reset = new Button("Reset", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    int intValue = nvl(camWidth.getConvertedValue(), 320);
                    webCam.setCamWidth(intValue);
                    webCam.setWidth(intValue, Unit.PIXELS);
                    intValue = nvl(camHeight.getConvertedValue(), 240);
                    webCam.setCamHeight(intValue);
                    webCam.setHeight(intValue, Unit.PIXELS);
                    webCam.setQuality(quality.getValue().intValue());
                } catch(Exception ex) {
                    Notification.show("Reset failed", 
                            ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        form.addComponent(reset);
        return layout;
    }
    
    private int nvl(Object value, int defValue) {
        if(value == null) {
            return defValue;
        }
        return (Integer)value;
    }
}
