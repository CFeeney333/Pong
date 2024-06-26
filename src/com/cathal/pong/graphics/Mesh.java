package com.cathal.pong.graphics;

import com.cathal.pong.utils.BufferUtils;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

    private int vaoID, vboID, iboID, tcoID;
    public static int POSITION_INDEX = 0, TCOORDS_INDEX = 1;
    private int count;
    private boolean hasTexCoords = false;

    public Mesh(float[] positions, byte[] indices, float[] textureCoordinates) {
        if (textureCoordinates != null) {
            hasTexCoords = true;
        }
        count = indices.length;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(positions), GL_STATIC_DRAW);
        glVertexAttribPointer(POSITION_INDEX, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(POSITION_INDEX);

        if (hasTexCoords) {
            tcoID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, tcoID);
            glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
            glVertexAttribPointer(TCOORDS_INDEX, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(TCOORDS_INDEX);
        }

        iboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void bind() {
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        bind();
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
    }

}
