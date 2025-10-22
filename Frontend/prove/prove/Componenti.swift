//
//  Componenti.swift
//  prove
//
//  Created by d4 on 17/10/25.
//

import SwiftUI

enum FormFieldType {
    case username
    case password
    case email
}

struct CustomTextField: View {
    let fieldType: FormFieldType
    @Binding var text: String
    let placeholder: String
    let errorMessage: String?
    @State private var isSecured: Bool = true
    private var hasError: Bool {
        errorMessage != nil && !text.isEmpty
    }
    
    private var systemIconName: String {
        switch fieldType {
        case .username: "person.fill"
        case .password: "lock.fill"
        case .email: "envelope.fill"
        }
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 5) {
            HStack(spacing: 12) {
                Image(systemName: systemIconName)
                    .foregroundColor(hasError ? .red : Color(.systemGray))
                    .frame(width: 20, alignment: .center)
                
                Group {
                    if fieldType == .password && isSecured {
                        SecureField(placeholder, text: $text)
                    } else {
                        TextField(placeholder, text: $text)
                            .keyboardType(fieldType == .email ? .emailAddress : .default)
                            .autocapitalization(.none)
                    }
                }
                .font(.body)
                
                if fieldType == .password {
                    Button(action: { isSecured.toggle() }) {
                        Image(systemName: isSecured ? "eye.slash" : "eye")
                            .foregroundColor(Color(.systemGray))
                    }
                }
            }
            .padding(15)
            .background(Color.white)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(hasError ? .red : Color(.systemGray4), lineWidth: 1.5)
            )
            .animation(.easeInOut(duration: 0.2), value: hasError)
            
            if hasError {
                Text(errorMessage!)
                    .font(.caption)
                    .foregroundColor(.red)
                    .padding(.leading, 10)
                    .transition(.opacity.animation(.easeIn))
            }
        }
    }
}


enum AppButtonStyle {
    case primary
    case text
}

struct CustomButton: View {
    let text: String
    let style: AppButtonStyle
    var isEnabled: Bool = true
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.headline)
                .frame(maxWidth: .infinity)
                .frame(height: 50)
                .background(style == .primary ? .indigo : .clear)
                .foregroundColor(style == .primary ? .white : .indigo)
                .cornerRadius(12)
        }
        .disabled(!isEnabled)
        .opacity(isEnabled ? 1.0 : 0.5)
        .animation(.easeOut(duration: 0.2), value: isEnabled)
    }
}
