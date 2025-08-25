@echo off
echo ========================================
echo 运行Beman项目单元测试
echo ========================================
echo.

echo 清理之前的测试结果...
if exist "target\surefire-reports" rmdir /s /q "target\surefire-reports"
if exist "target\test-classes" rmdir /s /q "target\test-classes"

echo.
echo 编译项目...
call mvnw clean compile test-compile

if %ERRORLEVEL% neq 0 (
    echo 编译失败，请检查代码错误
    pause
    exit /b 1
)

echo.
echo 运行单元测试...
call mvnw test

if %ERRORLEVEL% neq 0 (
    echo.
    echo ========================================
    echo 测试执行失败！
    echo 请检查测试报告：target\surefire-reports\
    echo ========================================
    pause
    exit /b 1
)

echo.
echo ========================================
echo 所有测试执行完成！
echo 测试报告位置：target\surefire-reports\
echo ========================================
echo.

echo 生成测试覆盖率报告...
call mvnw jacoco:report

echo.
echo 测试覆盖率报告位置：target\site\jacoco\
echo.

pause
