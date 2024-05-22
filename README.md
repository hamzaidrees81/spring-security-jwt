# spring-security-jwt
This project contains a working example of basic Spring Security project using
JWT Token.
The project contains endpoints and role-based authentication, along
with a postman collection to test the working.


How to try:
1. Run the Springboot project
2. Load the collection SecurityTest in postman

Trying out the collections:
Collections are numbers to keep the working in order.
1. Public endpoints: These endpoints do not need any authorization/token and should
   be accessible directly.
2. Authenticate - GetToken: This endpoint takes authentication payload and returns a token.
   For demo, the body is kept simple and the token role is an enum value passed
   as authority inside the json payload.
3. WIthout token: This calls a request which requires token and ends up being rejected
4. Configuration - Admin: this calls a configuration endpoint with role ADMIN which goes through successfully.
5. Configuration - Wrong role: Authenticate with role USER and use token to call this endpoint. Since role required is ADMIN but the passed role is USER, the request should be rejected.
6. History- User Role; This returns an endpoint available with role USER successfully.
