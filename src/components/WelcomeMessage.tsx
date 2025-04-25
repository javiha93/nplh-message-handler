
import React from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Check } from 'lucide-react';

const WelcomeMessage: React.FC = () => {
  const features = [
    "Vibrant community connections",
    "Creative innovation space",
    "Thoughtful design principles",
    "Warm and friendly atmosphere"
  ];

  return (
    <div id="welcome" className="py-24 bg-white">
      <div className="container px-4">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-5xl font-bold mb-6">
            Welcome to Our <span className="text-warm-500">Community</span>
          </h2>
          <p className="text-lg text-gray-600 max-w-3xl mx-auto">
            We believe in creating spaces where everyone feels welcomed, valued, and inspired.
            Our mission is to build bridges and foster connections across cultures and ideas.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-5xl mx-auto">
          <Card className="overflow-hidden border-none shadow-lg hover:shadow-xl transition-shadow duration-300">
            <CardContent className="p-8">
              <h3 className="text-2xl font-bold mb-4 text-warm-700">Our Vision</h3>
              <p className="text-gray-600 mb-6">
                We envision a world where every greeting opens doors to new possibilities,
                where "Hola" is more than a word—it's the beginning of something wonderful.
              </p>
              <div className="h-1 w-20 bg-warm-400 rounded"></div>
            </CardContent>
          </Card>

          <Card className="overflow-hidden border-none shadow-lg hover:shadow-xl transition-shadow duration-300">
            <CardContent className="p-8">
              <h3 className="text-2xl font-bold mb-4 text-warm-700">What We Offer</h3>
              <ul className="space-y-3">
                {features.map((feature, index) => (
                  <li key={index} className="flex items-center">
                    <span className="flex items-center justify-center bg-warm-100 rounded-full p-1 mr-3">
                      <Check className="h-4 w-4 text-warm-600" />
                    </span>
                    <span className="text-gray-600">{feature}</span>
                  </li>
                ))}
              </ul>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default WelcomeMessage;
