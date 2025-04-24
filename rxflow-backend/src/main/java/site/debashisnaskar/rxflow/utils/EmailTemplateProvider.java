package site.debashisnaskar.rxflow.utils;

import java.util.Map;

public class EmailTemplateProvider {

    public static String getAdminRegisterNotificationTemplate(Map<String, String> data) {

        String content = """
                <!DOCTYPE html>
                <html>
                  <body style="font-family: Arial, sans-serif; color: #333;">
                    <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                      <h2 style="color: #4CAF50;">🎉 New User Registered</h2>
                      <p>Hello Admin,</p>
                      <p>A new user has just registered on the system. Here are the details:</p>
                      <table style="width: 100%; border-collapse: collapse;">
                        <tr>
                          <td style="padding: 8px; font-weight: bold;">👤 Name:</td>
                          <td style="padding: 8px;"> {{name}}</td>
                        </tr>
                        <tr>
                          <td style="padding: 8px; font-weight: bold;">📧 Email:</td>
                          <td style="padding: 8px;"> {{email}} </td>
                        </tr>
                        <tr>
                          <td style="padding: 8px; font-weight: bold;">🔑 Role:</td>
                          <td style="padding: 8px;"> {{role}} </td>
                        </tr>
                      </table>
                      <p style="margin-top: 20px;">Regards,<br><strong>RxFlow System</strong></p>
                    </div>
                  </body>
                </html>
                """;

        return content.replace("{{name}}", data.get("name")).replace("{{email}}", data.get("email")).replace("{{role}}", data.get("role"));
    }

    public static String getWelcomeUserTemplate(String name) {
        String content = """
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <meta charset="UTF-8" />
                    <title>Welcome Email</title>
                    <style>
                      body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 0;
                      }
                      .container {
                        background-color: #ffffff;
                        max-width: 600px;
                        margin: 40px auto;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                      }
                      h2 {
                        color: #4CAF50;
                      }
                      .button {
                        display: inline-block;
                        padding: 12px 20px;
                        margin-top: 20px;
                        background-color: #4CAF50;
                        color: white;
                        text-decoration: none;
                        border-radius: 6px;
                        font-weight: bold;
                      }
                      .footer {
                        text-align: center;
                        margin-top: 30px;
                        font-size: 13px;
                        color: #888;
                      }
                    </style>
                  </head>
                  <body>
                    <div class="container">
                      <h2>👋 Welcome to RxFlow, {{name}}!</h2>
                      <p>We're thrilled to have you on board. 🚀</p>
                      <p>Your account has been successfully created, and you're now part of our growing medical appointment management system.</p>
                
                      <p>If you ever need help, we're just a click away.</p>
                
                      <a class="button" href="https://rxflow.debashisnaskar.site">Go to Dashboard</a>
                
                      <div class="footer">
                        &copy; 2025 RxFlow. All rights reserved.<br />
                        Built with ❤️ for seamless doctor-patient management.
                      </div>
                    </div>
                  </body>
                </html>
                """;

        content = content.replace("{{name}}", name);
        return content;
    }

    public static String getResetPasswordTemplate(String name, String resetLink) {
        String content = """
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <meta charset="UTF-8" />
                    <title>Password Reset</title>
                    <style>
                      body {
                        font-family: Arial, sans-serif;
                        background-color: #f7f7f7;
                        margin: 0;
                        padding: 0;
                      }
                      .container {
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #ffffff;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 0 10px rgba(0,0,0,0.05);
                      }
                      h2 {
                        color: #e53935;
                      }
                      .button {
                        display: inline-block;
                        padding: 12px 20px;
                        background-color: #e53935;
                        color: #fff;
                        text-decoration: none;
                        border-radius: 6px;
                        font-weight: bold;
                        margin-top: 20px;
                      }
                      .footer {
                        margin-top: 30px;
                        font-size: 13px;
                        color: #888;
                        text-align: center;
                      }
                    </style>
                  </head>
                  <body>
                    <div class="container">
                      <h2>🔒 Password Reset Requested</h2>
                      <p>Hello {{name}},</p>
                      <p>We received a request to reset your password. Click the button below to proceed. If you did not request this, you can safely ignore this email.</p>
                
                      <a class="button" href="{{reset_link}}">Reset Password</a>
                
                      <p style="margin-top: 30px;">This link will expire in 15 minutes for your security.</p>
                
                      <div class="footer">
                        &copy; 2025 RxFlow. Your privacy and security are our top priorities.
                      </div>
                    </div>
                  </body>
                </html>
                
                """;

        return content.replace("{{name}}", name).replace("{{reset_link}}", resetLink);
    }





}
