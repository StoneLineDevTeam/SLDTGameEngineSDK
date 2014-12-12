package net.sldt_team.assetsManager.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GAFCompiler {
    public static void compileAssetsFileFromZip(File toCompile, File compiled) throws IOException {
        FileInputStream in = new FileInputStream(toCompile);
        FileOutputStream out = new FileOutputStream(compiled);

        byte[] b = new byte[1024];
        int length;
        while ((length = in.read(b)) > 0){
            for (int i = 0 ; i < b.length ; i++){
                byte b1 = b[i];
                b[i] = (byte)(b1 ^ 1);
            }
            out.write(b, 0, length);
        }

        in.close();
        out.close();
    }
}
