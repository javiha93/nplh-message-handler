
import React from 'react';
import HeroSection from '../components/HeroSection';
import WelcomeMessage from '../components/WelcomeMessage';
import ContactSection from '../components/ContactSection';
import Footer from '../components/Footer';

const Index = () => {
  return (
    <div className="min-h-screen">
      <HeroSection />
      <WelcomeMessage />
      <ContactSection />
      <Footer />
    </div>
  );
};

export default Index;
