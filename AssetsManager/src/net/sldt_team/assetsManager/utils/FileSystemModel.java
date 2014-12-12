package net.sldt_team.assetsManager.utils;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author ProgrammingKnowledge
 */
public class FileSystemModel implements TreeModel {

    private File root;
    private Vector listeners = new Vector();
    private String rootName;

    public FileSystemModel(@NotNull File rootDirectory, String... rootDisplayName) {
        root = rootDirectory;
        if (rootDisplayName.length > 0) {
            rootName = rootDisplayName[0];
        } else {
            rootName = null;
        }
    }

    public Object getRoot() {
        if (rootName != null){
            return rootName;
        }
        return root;
    }

    public Object getChild(Object parent, int index) {
        if (parent instanceof String){
            parent = root;
        }
        File directory = (File) parent;
        List<File> children = Utilities.getValidFiles(directory);

        return new FileSystemModel.TreeFile(directory, children.get(index).getName());
    }

    public int getChildCount(Object parent) {
        if (parent instanceof String){
            parent = root;
        }
        File file = (File) parent;
        if (file.isDirectory()) {
            String[] fileList = file.list();

            if (fileList != null) {
                //return file.list().length;
                return Utilities.getValidFiles(file).size();
            }
        }
        return 0;
    }

    public boolean isLeaf(Object node) {
        if (node instanceof String){
            node = root;
        }
        File file = (File) node;
        return file.isFile();
    }

    public int getIndexOfChild(Object parent, Object child) {
        File directory = (File) parent;
        File file = (File) child;
        List<File> children =  Utilities.getValidFiles(directory);
        for (int i = 0; i < children.size(); i++) {
            if (file.getName().equals(children.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    public void valueForPathChanged(TreePath path, Object value) {
        File oldFile = (File) path.getLastPathComponent();
        String fileParentPath = oldFile.getParent();
        String newFileName = (String) value;
        File targetFile = new File(fileParentPath, newFileName);
        oldFile.renameTo(targetFile);
        File parent = new File(fileParentPath);
        int[] changedChildrenIndices = {getIndexOfChild(parent, targetFile)};
        Object[] changedChildren = {targetFile};
        fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
    }

    private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
        TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
        Iterator iterator = listeners.iterator();
        TreeModelListener listener = null;
        while (iterator.hasNext()) {
            listener = (TreeModelListener) iterator.next();
            listener.treeNodesChanged(event);
        }
    }

    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }

    private class TreeFile extends File {

        public TreeFile(File parent, String child) {
            super(parent, child);
        }

        public String toString() {
            return getName();
        }
    }
}