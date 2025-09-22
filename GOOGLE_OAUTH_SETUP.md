# Google OAuth Setup Guide for CoinCraft

This guide explains how to set up Google OAuth authentication for CoinCraft.

## Prerequisites

1. A Google account
2. Access to Google Cloud Console

## Step 1: Create Google Cloud Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Note your project ID

## Step 2: Enable Google+ API

1. In the Google Cloud Console, go to "APIs & Services" > "Library"
2. Search for "Google+ API" and enable it
3. Also enable "Google OAuth2 API"

## Step 3: Create OAuth2 Credentials

1. Go to "APIs & Services" > "Credentials"
2. Click "Create Credentials" > "OAuth 2.0 Client IDs"
3. If prompted, configure the OAuth consent screen:
   - Choose "External" user type
   - Fill in the required fields:
     - App name: "CoinCraft"
     - User support email: your email
     - Developer contact information: your email
   - Add scopes:
     - `../auth/userinfo.email`
     - `../auth/userinfo.profile`
   - Add test users (your email addresses)

4. For Application type, choose "Desktop application"
5. Name it "CoinCraft Desktop Client"
6. Click "Create"

## Step 4: Configure OAuth2 Client

1. After creating the OAuth2 client, you'll get:
   - Client ID (looks like: `123456789-abcdef.apps.googleusercontent.com`)
   - Client Secret (looks like: `GOCSPX-abcdefghijklmnop`)

2. Edit authorized redirect URIs:
   - Add: `http://localhost:8080/Callback`
   - Add: `http://localhost:8080`
   - Add: `urn:ietf:wg:oauth:2.0:oob` (for desktop apps)

## Step 5: Configure CoinCraft

1. Open `src/main/resources/google-oauth-config.properties`
2. Replace the placeholder values:
   ```properties
   google.oauth.client.id=YOUR_ACTUAL_CLIENT_ID.apps.googleusercontent.com
   google.oauth.client.secret=YOUR_ACTUAL_CLIENT_SECRET
   ```
3. Save the file

## Step 6: Test the Integration

1. Run CoinCraft
2. Select "Parent/Guardian" role
3. Click "SIGN IN WITH GOOGLE"
4. A browser window should open asking for Google authentication
5. Complete the OAuth flow
6. You should be redirected back to CoinCraft with successful authentication

## Troubleshooting

### Common Issues

1. **"redirect_uri_mismatch" error**
   - Make sure the redirect URI in Google Console matches exactly what's in the config file
   - Common URIs: `http://localhost:8080/Callback`

2. **"invalid_client" error**
   - Check that your Client ID and Client Secret are correct
   - Make sure there are no extra spaces or characters

3. **"access_denied" error**
   - The user cancelled the OAuth flow
   - Or the app is not authorized for the user's Google account

4. **"Browser doesn't open"**
   - On some systems, you may need to manually open the authorization URL
   - Check the console logs for the authorization URL

### Development vs Production

- **Development**: Use `http://localhost:8080/Callback` as redirect URI
- **Production**: Update the redirect URI to match your production domain
- **Desktop App**: Consider using `urn:ietf:wg:oauth:2.0:oob` for out-of-band flow

## Security Notes

1. **Never commit real credentials to version control**
2. **Use environment variables in production**
3. **Keep your Client Secret secure**
4. **Regularly rotate your OAuth credentials**

## Fallback Mode

If Google OAuth is not properly configured, CoinCraft will automatically fall back to demo mode, which simulates Google authentication for testing purposes. You'll see "(Demo Mode)" in the success message.

## Support

If you encounter issues:
1. Check the application logs for detailed error messages
2. Verify your Google Cloud Console configuration
3. Ensure your OAuth consent screen is properly configured
4. Test with a different Google account
