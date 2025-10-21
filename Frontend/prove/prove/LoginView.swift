//
//  ContentView.swift
//  prove
//
//  Created by d4 on 30/09/25.
//

import SwiftUI

struct LoginView : View {
    @State private var user = Credenziali()
    
    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Spacer()
                Image("Logo")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 150, height: 150)
                    .clipShape(RoundedRectangle(cornerRadius: 30))
                    .shadow(color: .black.opacity(0.1), radius: 10, y: 5)
                
                Text("Welcome Back")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                                
                VStack(spacing: 15) {
                    CustomTextField(
                        fieldType: .username,
                        text: $user.username,
                        placeholder: "Username",
                        errorMessage: user.usernameError()
                    )
                    
                    CustomTextField(
                        fieldType: .password,
                        text: $user.password,
                        placeholder: "Password",
                        errorMessage: user.passwordError()
                    )
                }.padding(.top)
                
                VStack(spacing: 12) {
                    CustomButton(
                        text: "Sign In",
                        style: .primary,
                        isEnabled: user.isValidForSignIn(),
                        action: { print("Sign In Tapped") }
                    )
                    
                    CustomButton(
                        text: "Clear",
                        style: .text,
                        action: { user.clear() }
                    )
                }.padding(.top)
                
                Spacer()
                
                VStack {
                    NavigationLink(destination: RegisterView()) {
                        HStack(spacing: 4) {
                            Text("Don't have an account?")
                            Text("Sign Up")
                                .fontWeight(.semibold)
                        }
                        .font(.footnote)
                        .foregroundColor(Color(.systemGray))
                    }
                }
                .padding(.bottom, 20)
            }
            .padding(.horizontal, 30)
        }.navigationBarBackButtonHidden()
    }
}

#Preview {
    LoginView()
}
