<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome to Test</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 650px;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            margin: auto;
            padding: 30px;
        }
        .header {
            background-color: #004080;
            color: white;
            padding: 20px;
            border-radius: 10px 10px 0 0;
            text-align: center;
        }
        .header h2 {
            margin: 0;
        }
        .content {
            padding: 20px 0;
        }
        .content p {
            font-size: 16px;
        }
        .summary-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .summary-table th, .summary-table td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .summary-table th {
            background-color: #f0f0f0;
        }
        .footer {
            font-size: 13px;
            color: #777;
            text-align: center;
            margin-top: 30px;
        }
        .footer p {
            margin: 5px 0;
        }
        .highlight {
            font-weight: bold;
            color: #d63384;
        }
        a.button {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #004080;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>🎉 Welcome to Test!</h2>
    </div>
    <div class="content">
        <p>Dear <strong>${userName}</strong>,</p>
        <p>We're excited to have you on board! Below are your login details:</p>

        <table class="summary-table">
            <tr>
                <th>Username</th>
                <td>${userName}</td>
            </tr>
            <tr>
                <th>Password</th>
                <td>${password}</td>
            </tr>
            <tr>
                <th>Login URL</th>
                <td><a href="${loginUrl}" target="_blank">${loginUrl}</a></td>
            </tr>
        </table>

        <a href="${loginUrl}" class="button">Login Now</a>

        <p class="notes" style="margin-top: 30px; background-color: #f9f9f9; border-left: 4px solid #17a2b8; padding: 15px;">
            <span class="highlight">Security Tip:</span> Please keep your credential confidential. Do not share it with anyone. If you suspect any unauthorized access, please change your password immediately.
        </p>

        <p>If you have any issues logging in, feel free to contact our support team at <a href="mailto:info@test.com">info@test.com</a>.</p>
    </div>

    <div class="footer">
        <p>Thank you,</p>
        <p><strong>Team Test Company</strong></p>
        <p><em>This is an automated email. Please do not reply to this message.</em></p>
    </div>
</div>
</body>
</html>
