Add-Type -AssemblyName System.Drawing

$src = 'C:\Users\Growth\.gemini\antigravity\brain\5921e6f2-9781-4865-8ce5-7ca3ea177b9b\gambit_icon_knight_olive_1795198638365.jpg'
$bmp = [System.Drawing.Bitmap]::FromFile($src)

Write-Host "Diagonal from (0,0):"
for ($i = 0; $i -lt 250; $i += 10) {
    $c = $bmp.GetPixel($i, $i)
    Write-Host "  ($i, $i): R=$($c.R), G=$($c.G), B=$($c.B)"
}
$bmp.Dispose()
