//
// © 2024-present https://github.com/cengiz-pz
//

package org.godotengine.plugin.android.godotimagecropper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.canhub.cropper.CropImageActivity;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;

import org.godotengine.godot.Dictionary;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class GodotImageCropperPlugin extends GodotPlugin {
    private static final String CLASS_NAME = GodotImageCropperPlugin.class.getSimpleName();
    private static final String LOG_TAG = "GODOT IMAGE CROPPER LOG: ";
    private static final String IMAGE_LOADED = "image_loaded";
    private Activity activity;
    private boolean have_permission = false;
    private static final int CROP_IMAGE_RESULT = 7;
    private CropImageOptions options;
    private CropImageContract contract;
    /**
     * Base constructor passing a {@link Godot} instance through which the plugin can access Godot's
     * APIs and lifecycle events.
     *
     * @param godot
     */
    public GodotImageCropperPlugin(Godot godot) {
        super(godot);
    }

    @Override
    protected Godot getGodot() {
        return super.getGodot();
    }

    @Override
    public void onMainActivityResult(int requestCode, int resultCode, Intent data) {
        super.onMainActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_IMAGE_RESULT) {
            CropImageView.CropResult result = contract.parseResult(resultCode, data);
            if (result.isSuccessful()){
                Log.d(LOG_TAG, "Yeahh sucesss");
                glideIt(result.getUriContent());
            }else{
                Log.e(LOG_TAG, "Nope failed");
            }
        }
    }

    private void glideIt(Uri resultUri) {
        Glide.with(activity).asBitmap().load(resultUri)
                .apply(new RequestOptions().override(350, 350))
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap bm = Bitmap.createScaledBitmap(resource, resource.getWidth(), resource.getHeight(), false);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        if (options.outputCompressFormat == Bitmap.CompressFormat.JPEG){
                            bm.compress(Bitmap.CompressFormat.JPEG, 75,out);
                        }else if (options.outputCompressFormat == Bitmap.CompressFormat.PNG){
                            bm.compress(Bitmap.CompressFormat.PNG, 75,out);
                        }else{
                            bm.compress(Bitmap.CompressFormat.WEBP, 75,out);
                        }
                        byte[] picData = out.toByteArray();
                        Dictionary dick = new Dictionary();
                        dick.put("0",picData);
                        emitSignal(IMAGE_LOADED, dick);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
    }

    private byte[] bitMaptoByteArray(Bitmap bm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
         if (options.outputCompressFormat == Bitmap.CompressFormat.JPEG){
                 bm.compress(Bitmap.CompressFormat.JPEG, 75,stream);
            }else if (options.outputCompressFormat == Bitmap.CompressFormat.PNG){
                 bm.compress(Bitmap.CompressFormat.PNG, 75,stream);
              }else{
                 bm.compress(Bitmap.CompressFormat.WEBP, 75,stream);
             }
            stream.close();
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @UsedByGodot
    public void setCropShape(String sh) {
        if (sh.equals("OVAL")) {
            options.cropShape = CropImageView.CropShape.OVAL;
        } else if (sh.equals("RECTANGLE")) {
            options.cropShape = CropImageView.CropShape.RECTANGLE;
        }
    }

    @UsedByGodot
    public void setMinCropSize(int min_width, int min_height) {
        options.minCropResultHeight = min_height;
        options.minCropResultWidth = min_width;
    }

    @UsedByGodot
    public void setMaxCropSize(int max_width, int max_height) {
        options.maxCropResultHeight = max_height;
        options.maxCropResultWidth = max_width;
    }

    @UsedByGodot
    public void setGuidelines(boolean b) {
        if (b) {
            options.guidelines = CropImageView.Guidelines.ON;
        } else {
            options.guidelines = CropImageView.Guidelines.OFF;
        }
    }

    @UsedByGodot
    public void includeGallery(boolean b) {
        options.imageSourceIncludeGallery = b;
    }

    @UsedByGodot
    public void includeCamera(boolean b) {
        options.imageSourceIncludeCamera = b;
    }

    @UsedByGodot
    public void fixAspectRatio(boolean b) {
        options.fixAspectRatio = b;
    }

    @UsedByGodot
    public void setAspectRatio(int x, int y) {
        options.aspectRatioX = x;
        options.aspectRatioY = y;
    }

    @UsedByGodot
    public void compressedFormat(String s) {
        if (s.equals("JPEG")) {
            options.outputCompressFormat = Bitmap.CompressFormat.JPEG;
        } else if (s.equals("PNG")) {
            options.outputCompressFormat = Bitmap.CompressFormat.PNG;
        } else if (s.equals("WEBP")) {
            options.outputCompressFormat = Bitmap.CompressFormat.WEBP;
        }
    }

    @UsedByGodot
    public void getImage() {
        contract = new CropImageContract();
        CropImageContractOptions cop = new CropImageContractOptions(Uri.EMPTY, options);
        activity.startActivityForResult(contract.createIntent(activity, cop), CROP_IMAGE_RESULT);

    }

    @NonNull
    @Override
    public String getPluginName() {
        return CLASS_NAME;
    }

    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new ArraySet<>();
        signals.add(new SignalInfo(IMAGE_LOADED, Dictionary.class));
        return signals;
    }

    @Nullable
    @Override
    public View onMainCreate(Activity activity) {
        this.activity = activity;
        options = new CropImageOptions();
        options.activityTitle = "Godot Image Cropper";
        return super.onMainCreate(activity);
    }


}


