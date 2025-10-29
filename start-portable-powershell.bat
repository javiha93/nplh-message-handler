@echo off
echo ========================================
echo Starting NPLH Message Handler (PowerShell)
echo ========================================
echo.
echo Starting server on port 8084...
echo Open your browser at: http://localhost:8084
echo.
echo Press Ctrl+C to stop the server
echo ========================================

cd /d "%~dp0"

REM Start PowerShell HTTP server
powershell -NoProfile -ExecutionPolicy Bypass -Command "& { $listener = New-Object System.Net.HttpListener; $listener.Prefixes.Add('http://localhost:8084/'); $listener.Start(); Write-Host 'Server running on http://localhost:8084'; Write-Host 'Press Ctrl+C to stop'; try { while ($listener.IsListening) { $context = $listener.GetContext(); $request = $context.Request; $response = $context.Response; $localPath = $request.Url.LocalPath; if ($localPath -eq '/') { $localPath = '/index.html' }; $filePath = Join-Path '%~dp0dist' $localPath.TrimStart('/'); if (Test-Path $filePath -PathType Leaf) { $content = [System.IO.File]::ReadAllBytes($filePath); $response.ContentLength64 = $content.Length; $response.OutputStream.Write($content, 0, $content.Length); } else { $response.StatusCode = 404; $errorContent = [System.Text.Encoding]::UTF8.GetBytes('404 - File Not Found'); $response.ContentLength64 = $errorContent.Length; $response.OutputStream.Write($errorContent, 0, $errorContent.Length); }; $response.OutputStream.Close(); } } catch { Write-Host 'Server stopped' } finally { $listener.Stop() } }"
