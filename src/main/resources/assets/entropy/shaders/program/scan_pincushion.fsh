#version 150

uniform sampler2D InSampler;

in vec2 texCoord;
in vec2 oneTexel;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

const vec4 Zero = vec4(0.0);
const vec4 Half = vec4(0.5);
const vec4 One = vec4(1.0);
const vec4 Two = vec4(2.0);

const float Pi = 3.1415926535;
const float PincushionAmount = 0.02;
const float ScanlineAmount = 0.8;
const float ScanlineScale = 1.0;
const float ScanlineHeight = 1.0;
const float ScanlineBrightScale = 1.0;
const float ScanlineOffset = 0.0;
const vec3 Floor = vec3(0.05, 0.05, 0.05);
const vec3 Power = vec3(0.8, 0.8, 0.8);

out vec4 fragColor;

void main() {
    vec4 InTexel = texture(InSampler, texCoord);

    vec2 PinUnitCoord = texCoord * Two.xy - One.xy;
    float PincushionR2 = pow(length(PinUnitCoord), 2.0);
    vec2 PincushionCurve = PinUnitCoord * PincushionAmount * PincushionR2;
    vec2 ScanCoord = texCoord;

    ScanCoord *= One.xy - PincushionAmount * 0.2;
    ScanCoord += PincushionAmount * 0.1;
    ScanCoord += PincushionCurve;

    // -- Alpha Clipping --
    if (ScanCoord.x < 0.0) discard;
    if (ScanCoord.y < 0.0) discard;
    if (ScanCoord.x > 1.0) discard;
    if (ScanCoord.y > 1.0) discard;

    // -- Scanline Simulation --
    float InnerSine = ScanCoord.y * InSize.y * ScanlineScale * 0.25;
    float ScanBrightMod = sin(InnerSine * Pi + ScanlineOffset * InSize.y * 0.25);
    float ScanBrightness = mix(1.0, (pow(ScanBrightMod * ScanBrightMod, ScanlineHeight) * ScanlineBrightScale + 1.0) * 0.5, ScanlineAmount);
    vec3 ScanlineTexel = InTexel.rgb * ScanBrightness;

    // -- Color Compression (increasing the floor of the signal without affecting the ceiling) --
    ScanlineTexel = Floor + (One.xyz - Floor) * ScanlineTexel;

    ScanlineTexel.rgb = pow(ScanlineTexel.rgb, Power);

    fragColor = vec4(ScanlineTexel.rgb, 1.0);
}
