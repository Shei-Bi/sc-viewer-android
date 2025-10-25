package com.daniillshei.sceditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.donutquine.editor.displayObjects.SpriteSheet;
import dev.donutquine.editor.renderer.gl.texture.GLTexture;
import dev.donutquine.editor.renderer.texture.Texture;
import dev.donutquine.renderer.impl.swf.objects.DisplayObject;
import dev.donutquine.renderer.impl.swf.objects.DisplayObjectFactory;
import dev.donutquine.resources.ResourceManager;
import dev.donutquine.swf.SupercellSWF;
import dev.donutquine.swf.exceptions.LoadingFaultException;
import dev.donutquine.swf.exceptions.UnableToFindObjectException;
import dev.donutquine.swf.exceptions.UnsupportedCustomPropertyException;
import dev.donutquine.swf.movieclips.MovieClipOriginal;
import dev.donutquine.swf.shapes.ShapeDrawBitmapCommand;
import dev.donutquine.swf.shapes.ShapeOriginal;
import dev.donutquine.swf.textures.SWFTexture;

public class MainActivity extends Activity {

    private MyGLSurfaceView glView;
    public SupercellSWF swf;
    public TextView title_txt;
    public String filename;
    public boolean loadingExtraFiles;
    private ProgressBar loading_bar;
    public TextView loading_txt;
    private CustomTable[] menuTables;
    private int currentMenuTableIndex;
    private LinearLayout tableContainer;
    private ScrollView scrollArea;
    private final ArrayList<SpriteSheet> spriteSheets = new ArrayList<>();
    private Button menu_object_button;
    private Button menu_texture_button;
    private Button menu_info_button;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout);

        ResourceManager.assetManager = getAssets();

        title_txt = findViewById(R.id.title_txt);

        // Right button (will show submenu)
        ImageButton openFileButton = findViewById(R.id.add_button);
        openFileButton.setOnClickListener(v -> {
            loadingExtraFiles = false;
            openFileSelector();
        });


        ImageButton settings_button = findViewById(R.id.settings_button);
        settings_button.setOnClickListener(this::showPopupMenu);

        glView = findViewById(R.id.glView);

        loading_bar = findViewById(R.id.loading_bar);
        loading_txt = findViewById(R.id.loading_txt);

        loading_bar.setVisibility(View.INVISIBLE);
        loading_txt.setVisibility(View.INVISIBLE);

        tableContainer = findViewById(R.id.table_container);
        scrollArea = findViewById(R.id.scroll_area);

        menu_object_button = findViewById(R.id.menu_object_button);
        menu_object_button.setOnClickListener(v -> {
            switchToMenuTable(0);
        });
        menu_info_button = findViewById(R.id.menu_info_button);
        menu_info_button.setOnClickListener(v -> {
            switchToMenuTable(2);
        });
        menu_texture_button = findViewById(R.id.menu_texture_button);
        menu_texture_button.setOnClickListener(v -> {
            switchToMenuTable(1);
        });

        var objectsTable = (CustomTable) getLayoutInflater().inflate(R.layout.table, scrollArea, false);
        var texturesTable = (CustomTable) getLayoutInflater().inflate(R.layout.table, scrollArea, false);
        var infoTable = (CustomTable) getLayoutInflater().inflate(R.layout.table, scrollArea, false);

        menuTables = new CustomTable[]{objectsTable, texturesTable, infoTable};

        tableContainer.post(this::initMenus);
    }

    private void initMenus() {
        CustomTable objectsTable = getObjectsTable();
        objectsTable.removeAllViews();
        objectsTable.setColumnsWidth(new float[]{0.2F, 0.6F, 0.2F}, tableContainer.getWidth());
        objectsTable.setHeader(new String[]{"Id", "Name", "Type"});

        CustomTable texturesTable = getTexturesTable();
        texturesTable.removeAllViews();
        texturesTable.setColumnsWidth(new float[]{0.1F, 0.3F, 0.3F, 0.3F}, tableContainer.getWidth());
        texturesTable.setHeader(new String[]{"Id", "Width", "Height", "Format"});

        CustomTable infoTable = getInfoTable();
        infoTable.removeAllViews();
        infoTable.setColumnsWidth(new float[]{0.3F, 0.6F}, tableContainer.getWidth());
        infoTable.setHeader(new String[]{"", "Click + to open a sc file!"});

        switchToMenuTable(2);
        setTableMaxHeight(dpToPx(300));
    }

    private void testOpenAssetSc(String name) {
        try (var sc = getAssets().open(name)) {
            var bytes1 = new byte[sc.available()];
            sc.read(bytes1);
            openFile(bytes1, name);
            if (loadingExtraFiles)
                try (var tex = getAssets().open(this.swf.getTextureFilepath(name))) {
                    var bytes2 = new byte[tex.available()];
                    tex.read(bytes2);
                    openFile(bytes2, name);
                } catch (Exception ignored) {
                }
        } catch (Exception ignored) {
        }
    }

    private void switchToMenuTable(int index) {
        if (tableContainer.getChildAt(0) instanceof LinearLayout)
            tableContainer.removeViewAt(0);
        tableContainer.addView(menuTables[index].header, 0);
        scrollArea.removeAllViews();
        scrollArea.addView(menuTables[index]);
//        tableContainer.requestLayout();
//        tableContainer.invalidate();
    }

    private void selectObjectInObjectTable(LinearLayout thatRow) {
        int id = Integer.parseInt(((TextView) thatRow.getChildAt(0)).getText().toString());
        DisplayObject displayObject;
        try {
            var displayObjectOriginal = this.swf.getOriginalDisplayObject(id, "hi");
            displayObject = DisplayObjectFactory.createFromOriginal(displayObjectOriginal, swf, null);
        } catch (UnableToFindObjectException e) {
            return;
        }

        //        if (objectIndex < 0) return;
//        if (objectIndex >= this.selectedIndices.size()) return;
//
//        this.selectedIndex = objectIndex;
//
//        DisplayObject displayObject = this.clonedObjects.get(this.selectedIndices.get(objectIndex));
//        window.getTimelinePanel().setSelectedObject(displayObject);
//
//        if (displayObject.isMovieClip()) {
//            this.window.setTargetFps(((MovieClip) displayObject).getFps());
//        }
//
//        this.window.updateInfoPanel(displayObject);
//
//        EditMenu editMenu = this.window.getMenubar().getEditMenu();
//        editMenu.checkPreviousAvailable();
//        editMenu.checkNextAvailable();
//

        StageGLES stage = StageGLES.getInstance();
        stage.doInRenderThread(() -> {
            stage.getCamera().reset();
            stage.clearBatches();
            stage.removeAllChildren();
            stage.addChild(displayObject);
        });
    }

    private void setTableMaxHeight(int px) {
        tableContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                px
        ));
    }

    public CustomTable getObjectsTable() {
        return menuTables[0];
    }

    public CustomTable getTexturesTable() {
        return menuTables[1];
    }

    public CustomTable getInfoTable() {
        return menuTables[2];
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenu().add(0, 1, 0, "events.sc");
        popup.getMenu().add(0, 2, 1, "background_chinaghost.sc");
        popup.getMenu().add(0, 3, 2, "loading.sc");

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    testOpenAssetSc("events.sc");
                    return true;
                case 2:
                    testOpenAssetSc("background_chinaghost.sc");
                    return true;
                case 3:
                    testOpenAssetSc("loading.sc");
                    return true;
            }
            return false;
        });

        popup.show();
    }

    // Handle menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Toast.makeText(this, "Reload clicked", Toast.LENGTH_SHORT).show();
                // Example: force GL surface reload
                glView.queueEvent(() -> glView.onPause());
                glView.queueEvent(() -> glView.onResume());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static final int REQUEST_CODE_PICK_FILE = 1;

    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // or "image/*", "video/*", etc.
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // allow multiple selection
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    private void requestFileWithDialog(String title, String text) {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(text)
//                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        openFileSelector();
//                    }
//                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        openFileSelector();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK && data != null) {
            if (loadingExtraFiles) {
                if (data.getData() != null)
                    openFile(data.getData());
                return;
            }
            ArrayList<Uri> uris = new ArrayList<>();
            if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    uris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                uris.add(data.getData());
            }
            String texScFileName = null;
            for (int i = 0; i < uris.size(); i++) {
                var uri = uris.get(i);
                var fileName = getFileName(uri);
                if (fileName.endsWith(".sc") && !fileName.endsWith("tex.sc")) {
                    openFile(uri);
                    texScFileName = this.swf.getTextureFilepath(fileName);
                    break;
                }
            }
            if (loadingExtraFiles) {
                if (texScFileName != null) {
                    for (int i = 0; i < uris.size(); i++) {
                        var uri = uris.get(i);
                        var fileName = getFileName(uri);
                        if (fileName.equals(texScFileName)) {
                            openFile(uri);
                            return;
                        }
                    }
                }
                requestFileWithDialog("Need Texture File", this.swf.getTextureFilepath(this.swf.getFilename()));
            }
        }
    }

    public void openFile(Uri uri) {
        try (InputStream input = getContentResolver().openInputStream(uri)) {
            byte[] inputBytes = new byte[input.available()];
            input.read(inputBytes);
            openFile(inputBytes, getFileName(uri));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            Toast.makeText(this, "Out Of Memory!", Toast.LENGTH_LONG).show();
        }
    }

    public void openFile(byte[] data, String filename) {
        if (filename.endsWith(".sc") || filename.endsWith(".sc2")) {
            if (!loadSc(data, filename)) {
                return;
            }
        } else if (filename.endsWith(".sctx")) {
            if (!loadSctx(data, filename)) {
                return;
            }
        }

        this.initMenus();
        List<GLTexture> images = uploadSwfTexturesToOpenGl();
        runInBackground(() -> {
            this.updateObjectTable();
            this.updateTextureTable(images);
        });
        title_txt.setText(filename);
//        FileMenu fileMenu = this.window.getMenubar().getFileMenu();
//        fileMenu.checkCanSave();
    }

    public void closeFile() {
        this.filename = null;

        this.spriteSheets.clear();

        StageGLES stage = StageGLES.getInstance();

        int textureCount = stage.getTextureCount();
        if (textureCount > 0) {
            int[] textureIds = new int[textureCount];
            for (int i = 0; i < textureCount; i++) {
                Texture texture = stage.getTextureByIndex(i);
                if (texture == null) continue;

                textureIds[i] = texture.getId();
            }

            stage.doInRenderThread(() -> stage.getGlContext().glDeleteTextures(textureIds.length, textureIds, 0));
        }
        stage.doInRenderThread(() -> {
            stage.clearBatches();
            stage.removeAllChildren();
        });
        this.swf = null;
//        this.sctxTexture = null;
    }

    private void updateObjectTable() {
        List<Object[]> rowDataList = collectObjectTableRows();

        runOnUiThread(() -> {
            createTaskTracker("Loading objects table...", rowDataList.size());
        });
        var objectsTable = getObjectsTable();
        for (int i = 0; i < rowDataList.size(); i++) {
            Object[] objects = rowDataList.get(i);
            int finalI = i;
            runOnUiThread(() -> {
                updateLoadingProgress(finalI + 1);
                objectsTable.addRow(objects).setOnClickListener(v -> {
                    selectObjectInObjectTable((LinearLayout) v);
                });
            });
        }
    }

    private List<Object[]> collectObjectTableRows() {
        List<Object[]> rowDataList = new ArrayList<>();

        int[] movieClipsIds = this.swf.getMovieClipIds();

        runOnUiThread(() -> {
            createTaskTracker("Collecting MovieClip info...", movieClipsIds.length);
        });
        for (int i = 0; i < movieClipsIds.length; i++) {
            int movieClipId = movieClipsIds[i];

            try {
                MovieClipOriginal movieClipOriginal = this.swf.getOriginalMovieClip(movieClipId & 0xFFFF, null);
                rowDataList.add(new Object[]{movieClipId, movieClipOriginal.getExportName(), "MovieClip"});
            } catch (UnableToFindObjectException e) {
                LOGGER.error(e.getMessage(), e);
            }
            int finalI = i;
            runOnUiThread(() -> {
                updateLoadingProgress(finalI + 1);
            });
        }

        int[] shapesIds = this.swf.getShapeIds();
        runOnUiThread(() -> {
            createTaskTracker("Collecting Shape info...", shapesIds.length);
        });
        for (int i = 0; i < shapesIds.length; i++) {
            int shapesId = shapesIds[i];
            rowDataList.add(new Object[]{shapesId, null, "Shape"});
            int finalI = i;
            runOnUiThread(() -> {
                updateLoadingProgress(finalI + 1);
            });
        }

        int[] textFieldsIds = this.swf.getTextFieldIds();
        runOnUiThread(() -> {
            createTaskTracker("Collecting TextField info...", textFieldsIds.length);
        });
        for (int i = 0; i < textFieldsIds.length; i++) {
            int textFieldId = textFieldsIds[i];
            rowDataList.add(new Object[]{textFieldId, null, "TextField"});
            int finalI = i;
            runOnUiThread(() -> {
                updateLoadingProgress(finalI + 1);
            });
        }

        return rowDataList;
    }

    private void updateTextureTable(List<GLTexture> images) {
//        Table texturesTable = this.window.getTexturesTable();
//        StatusBar statusBar = this.window.getStatusBar();

        runOnUiThread(() -> {
            createTaskTracker("Loading textures table...", images.size());
        });
        for (int i = 0; i < images.size(); i++) {
            GLTexture texture = images.get(i);
//            var row = new TextView(this);
//            row.setText(i);
//            table.addView(row);

//            texturesTable.addRow(i, texture.getWidth(), texture.getHeight(), texture.getFormat());

            SpriteSheet spriteSheet = new SpriteSheet(texture, getDrawBitmapsOfTexture(i));
            this.spriteSheets.add(spriteSheet);

            int finalI = i;
            runOnUiThread(() -> {
                updateLoadingProgress(finalI + 1);
                getTexturesTable().addRow(finalI, texture.getWidth(), texture.getHeight(), texture.getFormat()).setOnClickListener(v -> {
                    StageGLES stage = StageGLES.getInstance();
                    stage.doInRenderThread(() -> {
                        stage.getCamera().reset();
                        stage.clearBatches();
                        stage.removeAllChildren();
                        stage.addChild(this.spriteSheets.get(finalI));
                    });
                });
            });
        }
    }

    private void createTaskTracker(String loadingText, int maxProgress) {
        loading_txt.setVisibility(View.VISIBLE);
        loading_txt.setText(loadingText);
        loading_bar.setVisibility(View.VISIBLE);
        loading_bar.setIndeterminate(false);
        loading_bar.setMax(maxProgress);
        loading_bar.setProgress(0);
    }

    private void updateLoadingProgress(int progress) {
        loading_bar.setProgress(progress);
        if (progress >= loading_bar.getMax()) {
            loading_txt.setVisibility(View.INVISIBLE);
            loading_bar.setVisibility(View.INVISIBLE);
        }
    }

    private List<ShapeDrawBitmapCommand> getDrawBitmapsOfTexture(int textureIndex) {
        if (swf == null) {
            return Collections.emptyList();
        }

        List<ShapeDrawBitmapCommand> bitmapCommands = new ArrayList<>();

        for (ShapeOriginal shape : swf.getShapes()) {
            for (ShapeDrawBitmapCommand command : shape.getCommands()) {
                if (command.getTextureIndex() == textureIndex) {
                    bitmapCommands.add(command);
                }
            }
        }

        return bitmapCommands;
    }

    private List<GLTexture> uploadSwfTexturesToOpenGl() {
        List<GLTexture> images = new ArrayList<>();

        StageGLES stage = StageGLES.getInstance();
        for (int i = 0; i < this.swf.getTextureCount(); i++) {
            SWFTexture texture = this.swf.getTexture(i);
            GLTexture image = stage.createGLTexture(texture, this.swf.getPath().getParent());
            images.add(image);
        }

        return images;
    }

    private boolean loadSctx(byte[] data, String filename) {
//        try {
//            sctxTexture = loadSctxTexture(path);
//
//            List<GLTexture> images = uploadSctxTextureToOpenGl(sctxTexture);
//
//            SwingUtilities.invokeLater(() -> this.updateTextureTable(images));
//            return true;
//        } catch (TextureFileNotFound e) {
//            this.window.showErrorDialog(e.getMessage());
        return false;
//        } catch (Throwable e) {
//            ExceptionDialog.showExceptionDialog(Thread.currentThread(), e);
//            throw new RuntimeException(e);
//        }
    }

    private boolean loadSc(byte[] data, String filename) {
        try {
            if (!loadingExtraFiles) {
                closeFile();
                this.swf = new SupercellSWF();
                this.swf.loadInternalMemory(filename, data, false, false);
                if (this.swf.isUseExternalTexture()) {
                    loadingExtraFiles = true;
                    return false;
                }
                return true;
            } else {
                this.swf.loadInternalMemory(filename, data, true, false);
                loadingExtraFiles = false;
                return true;
            }
        } catch (LoadingFaultException | UnableToFindObjectException |
                 UnsupportedCustomPropertyException exception) {
            LOGGER.error("An error occurred while loading the file: {}", filename, exception);
//            ExceptionDialog.showExceptionDialog(Thread.currentThread(), exception);
            return false;
        } catch (Throwable e) {
//            ExceptionDialog.showExceptionDialog(Thread.currentThread(), e);
            throw new RuntimeException(e);
        }
    }

    // Get display name from content URI
    private String getFileName(Uri uri) {
        String name = null;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        name = cursor.getString(index);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return name;
    }

    private void runInBackground(Runnable task) {
        new Thread(task).start();
    }
}