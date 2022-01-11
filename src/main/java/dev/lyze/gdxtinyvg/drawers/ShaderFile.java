package dev.lyze.gdxtinyvg.drawers;

public class ShaderFile {
    // Source files located in src/resources/dev/lyze/gdxtinyvg/
    public static String vertex = "attribute vec4 a_position;\n" + "attribute vec4 a_color;\n"
            + "attribute vec2 a_texCoord0;\n" + "\n" + "uniform mat4 u_projTrans;\n" + "\n" + "varying vec4 v_color;\n"
            + "varying vec2 v_texCoords;\n" + "varying vec2 v_basePosition;\n" + "\n" + "void main()\n" + "{\n"
            + "    v_color = a_color;\n" + "    v_texCoords = a_texCoord0;\n"
            + "    gl_Position =  u_projTrans * a_position;\n" + "    v_basePosition = a_position.xy;\n" + "}";

    public static String fragment = "#ifdef GL_ES\n" + "precision mediump float;\n" + "#endif\n" + "\n"
            + "const float gamma = 2.2;\n" + "\n" + "varying vec4 v_color;\n" + "varying vec2 v_texCoords;\n" + "\n"
            + "uniform vec4 u_startColor;\n" + "uniform vec4 u_endColor;\n" + "\n" + "uniform vec2 u_startPosition;\n"
            + "uniform vec2 u_endPosition;\n" + "uniform vec2 u_resolution;\n" + "\n" + "uniform int u_style;\n" + "\n"
            + "uniform sampler2D u_texture;\n" + "\n" + "varying vec2 v_basePosition;\n" + "\n"
            + "vec4 linear2gamma(vec4 color) {\n" + "    return vec4(pow(color.rgb, vec3(1.0 / gamma)), color.a);\n"
            + "}\n" + "\n" + "vec4 gamma2linear(vec4 color) {\n"
            + "    return vec4(pow(color.rgb, vec3(gamma)), color.a);\n" + "}\n" + "\n"
            + "vec4 gammaMix(vec4 startColor, vec4 endColor, float v) {\n"
            + "    return gamma2linear(mix(linear2gamma(startColor), linear2gamma(endColor), v));\n" + "}\n" + "\n"
            + "vec2 getProjectedPointOnLine(vec2 v1, vec2 v2, vec2 p)\n" + "{\n" + "    // get dot product of e1, e2\n"
            + "    vec2 e1 = v2 - v1;\n" + "    vec2 e2 = p - v1;\n" + "    float valDp = dot(e1, e2);\n" + "\n"
            + "    // get length of vectors\n" + "    float lenLineE1 = sqrt(e1.x * e1.x + e1.y * e1.y);\n"
            + "    float lenLineE2 = sqrt(e2.x * e2.x + e2.y * e2.y);\n"
            + "    float cos = valDp / (lenLineE1 * lenLineE2);\n" + "\n" + "    // length of v1P'\n"
            + "    float projLenOfLine = cos * lenLineE2;\n" + "\n"
            + "    return vec2((v1.x + (projLenOfLine * e1.x) / lenLineE1), (v1.y + (projLenOfLine * e1.y) / lenLineE1));\n"
            + "}\n" + "\n" + "vec4 flatColor() {\n" + "    return u_startColor;\n" + "}\n" + "\n"
            + "vec4 linearGradient() {\n" + "    vec2 direction = u_endPosition - u_startPosition;\n"
            + "    vec2 delta_pt = v_basePosition.xy - u_startPosition;\n" + "\n"
            + "    if (dot(direction, delta_pt) <= 0.0)\n" + "        return u_startColor;\n" + "\n"
            + "    if (dot(direction, v_basePosition.xy - u_endPosition) >= 0.0)\n" + "        return u_endColor;\n"
            + "\n" + "    float len_grad = length(direction);\n"
            + "    float pos_grad = length(getProjectedPointOnLine(vec2(0, 0), direction, delta_pt));\n" + "\n"
            + "    return gammaMix(u_startColor, u_endColor, pos_grad / len_grad);\n" + "}\n" + "\n"
            + "vec4 radialGradient() {\n" + "    float len_total = length(u_startPosition - u_endPosition);\n"
            + "    float len_arc = length(u_startPosition - v_basePosition.xy);\n" + "\n"
            + "    float f = clamp(len_arc, 0.0, len_total) / len_total;\n" + "\n"
            + "    return gammaMix(u_startColor, u_endColor, f);\n" + "}\n" + "\n" + "void main() {\n"
            + "    if (u_style == 0)\n" + "        gl_FragColor = flatColor();\n" + "    else if (u_style == 1)\n"
            + "        gl_FragColor = linearGradient();\n" + "    else if (u_style == 2)\n"
            + "        gl_FragColor = radialGradient();\n" + "    else\n" + "        gl_FragColor = vec4(1, 0, 1, 1);\n"
            + "\n" + "    gl_FragColor *= v_color * texture2D(u_texture, v_texCoords);\n" + "}\n";
}
