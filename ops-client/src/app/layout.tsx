import './globals.css';
import { Inter } from 'next/font/google';
import { AuthProvider } from '@/hooks/useAuth';

const inter = Inter({ subsets: ['latin', 'thai'] });

export const metadata = {
    title: 'YIP E-INVENTORY',
    description: 'ระบบจัดการคลังสินค้าและทรัพยากร',
};

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html lang="th">
        <body className={inter.className}>
        <AuthProvider>
            {children}
        </AuthProvider>
        </body>
        </html>
    );
}