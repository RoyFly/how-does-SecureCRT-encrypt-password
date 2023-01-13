package finalShell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路径类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnPath {
    public enum PathType {
        //文件夹，
        DIR,
        //文件
        FILE,
    }

    /**
     * 路径名称
     */
    private String fullName;

    /**
     * 路径类型
     */
    private PathType pathType;

    /**
     * 是否是文件
     *
     * @return
     */
    public boolean isFile() {
        return PathType.FILE.equals(pathType);
    }
}
