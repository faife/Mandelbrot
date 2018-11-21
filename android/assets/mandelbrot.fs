#ifdef GL_ES
    precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float zoom;
uniform vec2 center;
uniform int maxIterations;
uniform float ratio;


void main()
{
    vec2 z, c;

        c.x = ratio * (v_texCoords.x - 0.5) * zoom + center.x;
        c.y = (v_texCoords.y - 0.5) * zoom - center.y;

        int i;
        z = c;
        for(i = 0; i < maxIterations; i++) {
            float x = (z.x * z.x - z.y * z.y) + c.x;
            float y = (z.y * z.x + z.x * z.y) + c.y;

            if(dot(z, z) > 4.0) break;
            z.x = x;
            z.y = y;
        }

        gl_FragColor = texture2D(u_texture, vec2(i == maxIterations ? 0.0 : float(i) / 128.0, 0));
}