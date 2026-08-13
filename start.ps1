# ============================================================
#  Clothing Store ERP - Backend Starter Script
#  Run this file to start the Spring Boot server
# ============================================================

$JAVA_HOME = "C:\Program Files\Java\jdk-17"
$MAVEN_DIR  = "$env:USERPROFILE\.m2\wrapper\apache-maven-3.9.6"
$MAVEN_ZIP  = "$env:TEMP\apache-maven-3.9.6-bin.zip"
$MAVEN_URL  = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"

# ── 1. Verify Java ────────────────────────────────────────────
if (-not (Test-Path "$JAVA_HOME\bin\java.exe")) {
    Write-Host "ERROR: Java not found at $JAVA_HOME" -ForegroundColor Red
    exit 1
}
$env:JAVA_HOME = $JAVA_HOME
$env:PATH = "$JAVA_HOME\bin;$env:PATH"
Write-Host "Java 17 found ✓" -ForegroundColor Green

# ── 2. Download Maven if missing ──────────────────────────────
if (-not (Test-Path "$MAVEN_DIR\bin\mvn.cmd")) {
    Write-Host "Downloading Apache Maven 3.9.6 (first time only)..." -ForegroundColor Yellow
    Invoke-WebRequest -Uri $MAVEN_URL -OutFile $MAVEN_ZIP -UseBasicParsing
    Expand-Archive -Path $MAVEN_ZIP -DestinationPath "$env:USERPROFILE\.m2\wrapper" -Force
    Remove-Item $MAVEN_ZIP -ErrorAction SilentlyContinue
    Write-Host "Maven downloaded ✓" -ForegroundColor Green
} else {
    Write-Host "Maven found ✓" -ForegroundColor Green
}
$env:PATH = "$MAVEN_DIR\bin;$env:PATH"

# ── 3. Set Database Config ─────────────────────────────────────
#    CHANGE THESE VALUES TO MATCH YOUR MYSQL SETUP
$env:DB_HOST     = "localhost"
$env:DB_PORT     = "3306"
$env:DB_NAME     = "clothing_erp"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = ""     # <-- PUT YOUR MYSQL ROOT PASSWORD HERE
$env:JWT_SECRET  = "dGhpcyBpcyBhIHZlcnkgbG9uZyBzZWNyZXQga2V5IGZvciBqd3QgYXV0aGVudGljYXRpb24gc2VjdXJpdHk="

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  Starting Clothing Store ERP Backend..." -ForegroundColor Cyan
Write-Host "  URL: http://localhost:8080"              -ForegroundColor Cyan
Write-Host "  Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# ── 4. Run Spring Boot ────────────────────────────────────────
Set-Location $PSScriptRoot
& "$MAVEN_DIR\bin\mvn.cmd" spring-boot:run
